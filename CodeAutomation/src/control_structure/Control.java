package control_structure;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import string_operations.StrOps;
import tools.Basics;
import logging.Log;
import file_operations.FileOps;
import java_parser.JParser;
import command_line.execute;

public class Control {
	public static void main(String[] args){
		System.out.println("Starting Attachment Downloader Engine.");
		System.out.println("Starting logger.");
		Log ll = new Log(DEFINE.LOGGING_LEVEL, DEFINE.LOG_FILE_PATH);
		ll.write(2, "\r\nStarting Attachment Downloader Engine v0.0\r\n");
		
		int RESULT_VALUE = 152;
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		ll.write(2, "Starting at " + dateFormat.format(new Date()) + "\r\n");
		long time_start = System.currentTimeMillis(), time_elapsed;
		
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Setting Global Variables.\r\n");
		ArrayList<String> students = new ArrayList<String>();
		ArrayList<Boolean> q3_exists = new ArrayList<Boolean>();
		ArrayList<Boolean> q4_exists = new ArrayList<Boolean>();
		ArrayList<Boolean> running_median_exists = new ArrayList<Boolean>();
		ArrayList<Boolean> compilation = new ArrayList<Boolean>();
		ArrayList<Boolean> compilation4 = new ArrayList<Boolean>();
		ArrayList<Boolean> resultCorrect = new ArrayList<Boolean>();
		ll.write(2, "Global Variables Set. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");

		
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Get Student Files.\r\n");
		ArrayList<String> files = FileOps.getAllPathsInDirectory(DEFINE.BASE_DIRECTORY);
		ll.write(2, "Student Files Obtained. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");
		
		/*
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Q3 runningMedian existance.\r\n");
		for(int ii = 0; ii < files.size(); ii++){
			ll.write(2, "Dealing with " + StrOps.getDilineatedSubstring(files.get(ii), "\\", 0, true) + "\n");
			students.add(StrOps.getDilineatedSubstring(files.get(ii), "\\", 0, true));
			String path = files.get(ii) + "\\q3";
			if(FileOps.directoryExists(path)){
				q3_exists.add(true);
				ArrayList<String> code_files = FileOps.getAllPathsInDirectory(path);
				JParser jp = new JParser();
				for(int jj = 0; jj < code_files.size(); jj++)
					jp.readFile(code_files.get(jj));
				jp.renameMethodVariable("&lt;", "<");
				jp.renameMethodVariable("&gt;", ">");
				jp.renameMethodVariable("&amp;", "&");
				if(jp.findString("runningMedian"))
					running_median_exists.add(true);
				else 
					running_median_exists.add(false);
				
				//get all java file names 
				ArrayList<String> java_files = new ArrayList<String>();
				for(int jj = 0; jj < code_files.size(); jj++){
					String jfile = StrOps.getDilineatedSubstring(code_files.get(jj), "\\", 0, true);
					if(StrOps.getDilineatedSubstring(jfile, ".", 1, false).equals("java"))
						java_files.add(jfile);
				}
				if(!execute.JavaCompile(path, java_files, false))
					compilation.add(false);
				else
					compilation.add(true);
			}
			else{
				q3_exists.add(false);
				running_median_exists.add(false);
				compilation.add(false);
			}
			// Basics.delay(5000);
		}
		ll.write(2, "Q3 runningMedian checked. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");
		*/
		
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Q4 test.\r\n");
		for(int ii = 0; ii < files.size(); ii++){
			ll.write(2, StrOps.getDilineatedSubstring(files.get(ii), "\\", 0, true) + "\n");
			FileOps.writeToFile(DEFINE.Q4_OUTPUT_FILE, "\n" + StrOps.getDilineatedSubstring(files.get(ii), "\\", 0, true), true);
			students.add(StrOps.getDilineatedSubstring(files.get(ii), "\\", 0, true));
			String path = files.get(ii) + "\\q4";
			if(FileOps.directoryExists(path)){
				q4_exists.add(true);
				ArrayList<String> code_files = FileOps.getAllPathsInDirectory(path);
				if(code_files.size() > 1){
					System.out.println("Too Many files. Skipping.");
					FileOps.writeToFile(DEFINE.Q4_OUTPUT_FILE, "\tToo Many files. Skipping.\t", true);
				}
				else{
					JParser jp = new JParser();
					jp.suppress(true);
					for(int jj = 0; jj < code_files.size(); jj++)
						jp.readFile(code_files.get(jj));
					jp.renameMethodVariable("&lt;", "<");
					jp.renameMethodVariable("&gt;", ">");
					jp.renameMethodVariable("&amp;", "&");
					
					ArrayList<String> classes = jp.getClasses();
					if(classes.size() > 3){
						System.out.println("Too Many Classes. Skipping.");
						FileOps.writeToFile(DEFINE.Q4_OUTPUT_FILE, "\tToo Many Classes. Skipping.\t", true);
					}
					else{		
						jp.renameClass(code_files.get(0), classes.get(0), "myBrain");
						ArrayList<String> include_code = FileOps.getAllPathsInDirectory(DEFINE.Q4_TESTING_CODE_DIRECTORY);
						FileOps.copyFiles(include_code, path);
						execute.JavaCompilePackage(path, false, true, "EC504.samegame.sg", DEFINE.Q4_OUTPUT_FILE);
						/*
						if(!execute.JavaCompile(path, java_files, false))
							compilation.add(false);
						else
							compilation.add(true);
							*/
					}
				}
			}
			else{
				q4_exists.add(false);
				compilation4.add(false);
				resultCorrect.add(false);
			}
			// Basics.delay(5000);
		}
		ll.write(2, "Q4 test. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");

		time_elapsed = System.currentTimeMillis();
	/*	ll.write(2, "Results.\r\n");
		ll.write(2, "\n\n");
		ll.write(2, "Compilation failed\n");
		for(int ii = 0; ii < students.size(); ii++){
			if(!compilation.get(ii))
				ll.write(2, students.get(ii) + "\n");
		}
		ll.write(2, "\n\n");
		for(int ii = 0; ii < students.size(); ii++){
			ll.write(2, students.get(ii) + "\t");
			if(q3_exists.get(ii)){
				ll.write(2, "1\t");
				if(compilation.get(ii)){
					ll.write(2, "1\t");
					if(running_median_exists.get(ii))
						ll.write(2,"1");
					else
						ll.write(2,"0");
				} else{
					ll.write(2,"0\t0");
				}
					
			} else{
				ll.write(2, "0\t0\t0");
			}
			ll.write(2, "\n");
		}
		ll.write(2, "Results Given. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");
		*/


		ll.write(2, "All operations complete at " + dateFormat.format(new Date()) + ". Took " + (System.currentTimeMillis() - time_start) + "ms.\r\n");
		ll.write(2, "Performing cleanup.\r\n\r\n");
		time_elapsed = System.currentTimeMillis();
		

		ll.close();
		
		System.out.println("Cleanup complete. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.");
		System.out.println("All operations complete.");
	}
}
