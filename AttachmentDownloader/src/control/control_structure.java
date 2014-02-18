package control;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import logging.Log;
import web.Client;
import basics.DEFINE;
import parser.LinkFileParser;
import string_operations.StrOps;
import robot.Robot_controller;
import file_operations.FileOps;
import tools.Basics;


public class control_structure {
	public static void main(String[] args){
		System.out.println("Starting Attachment Downloader Engine.");
		System.out.println("Starting logger.");
		Log ll = new Log(DEFINE.LOGGING_LEVEL, DEFINE.LOG_FILE_PATH);
		ll.write(2, "\r\nStarting Attachment Downloader Engine v0.0\r\n");
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		ll.write(2, "Starting at " + dateFormat.format(new Date()) + "\r\n");
		long time_start = System.currentTimeMillis(), time_elapsed;
		
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Starting Client.\r\n");
		Client cc = new Client(DEFINE.BASE_URL);
		ll.write(2, "Client ready. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");
		
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Logging in.\r\n");
		cc.sendKeys("username", DEFINE.USERNAME, true);
		cc.sendKeys("password", DEFINE.PASSWORD, true);
		cc.submit("password", true);
		ll.write(2, "Login complete. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");
		
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Parsing Links file.\r\n");
		LinkFileParser lpf = new LinkFileParser();
		lpf.parse(DEFINE.LINKS_FILE);
		ll.write(2, "Parsing complete. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");				
		
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Obtaining Code Files.\r\n");
		ArrayList<String> no_code_names = new ArrayList<String>();
		ArrayList<String> extensions = new ArrayList<String>();
		HashMap<String, Integer> extension_stats = new HashMap<String, Integer>();
		for(int ii = 0; ii < lpf.names.size(); ii++){
			boolean folder_made = false;
			String directory = "";
			ll.write(2, "Obtaining code files for " + lpf.names.get(ii) + "\r\n");
			cc.getPage(lpf.links.get(ii));
			String source = cc.getPageSource();
			if(StrOps.findPattern(source, DEFINE.ATTACH_TABLE_START_STRING) != -1){
				source = StrOps.get_substring_between_patterns(source, DEFINE.ATTACH_TABLE_START_STRING, DEFINE.ATTACH_TABLE_END_STRING);
				source = StrOps.get_substring_between_patterns(source, DEFINE.ATTACH_TABLE_START_STRING_LEVEL_2	,DEFINE.ATTACH_TABLE_END_STRING_LEVEL_2);
				int link_index = StrOps.findPattern(source, DEFINE.ATTACH_LINK_START_PATTERN);
				while(link_index != -1){
					source = source.substring(link_index+DEFINE.ATTACH_LINK_START_PATTERN.length());
					int end_pattern_index = StrOps.findPattern(source, DEFINE.ATTACH_LINK_END_PATTERN);
					if(end_pattern_index != -1){
						String link = source.substring(0,end_pattern_index);
						String extension = StrOps.getDilineatedSubstring(link, ".", 0, true);
						if(extension_stats.containsKey(extension))
							extension_stats.put(extension, extension_stats.get(extension) + 1);
						else{
							extensions.add(extension);
							extension_stats.put(extension, 1);
						}
						
						if(StrOps.findPattern(link, DEFINE.LINK_CONTAINS) != -1 && (StrOps.findPattern(link, DEFINE.FILE_EXTENSION) != -1 || StrOps.findPattern(link, DEFINE.FILE_EXTENSION2) != -1)){
							
							if(!folder_made){
								directory = FileOps.createFolder(DEFINE.BASE_DIRECTORY, lpf.names.get(ii));
								folder_made = true;
							}
							ll.write(2, "Found link " + link + "\r\n");
							cc.getPage(link);
							String file_name = StrOps.getDilineatedSubstring(link, "/", 0, true);
							if(StrOps.findPattern(link, DEFINE.FILE_EXTENSION) != -1){
								ll.write(2, "Acquiring contents\r\n");
								String file = cc.getPageSource();
								if(StrOps.findPattern(file, "<pre>") != -1)
									file = StrOps.get_substring_between_patterns(file, "<pre>", "</pre>");
								FileOps.writeToFile(directory + "\\" + file_name, file, false);
							}
							else if(StrOps.findPattern(link, DEFINE.FILE_EXTENSION2) != -1){
								Basics.delay(3000);
								FileOps.moveFile(DEFINE.DOWNLOADS_FOLDER + "\\" + file_name, directory + "\\" + file_name);
							}
						}
					}
					link_index = StrOps.findPattern(source, DEFINE.ATTACH_LINK_START_PATTERN);
				}
			} 
			if(!folder_made){
				ll.write(2, "No code files for " + lpf.names.get(ii) + "\r\n");
				no_code_names.add(lpf.names.get(ii));
			}
		}
		ll.write(2, "\r\nCode not obtained for " + no_code_names.size() + " people:\r\n");
		for(int ii = 0; ii < no_code_names.size(); ii++){
			ll.write(2, no_code_names.get(ii) + "\r\n");
		}
		ll.write(2, "\r\n\r\nExtensions Seen:\r\n");
		for(int ii = 0; ii < extensions.size(); ii++){
			ll.write(2, extensions.get(ii) + ": " + extension_stats.get(extensions.get(ii))+"\r\n");
		}
		ll.write(2, "Files Obtained. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");	

		
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Sorting Files.\r\n");
		
		ll.write(2, "Files Obtained. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");	
		

		ll.write(2, "All operations complete at " + dateFormat.format(new Date()) + ". Took " + (System.currentTimeMillis() - time_start) + "ms.\r\n");
		ll.write(2, "Performing cleanup.\r\n\r\n");
		time_elapsed = System.currentTimeMillis();
		
		cc.close();
		ll.close();
		
		System.out.println("Cleanup complete. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.");
		System.out.println("All operations complete.");
	}
}
