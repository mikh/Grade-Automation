package parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import logging.Log;
import string_operations.StrOps;
import basics.DEFINE;

public class LinkParser {
	String source;
	Log ll;
	ArrayList<String> names;
	ArrayList<String> links;
	int list_index;
	
	public LinkParser(String source, Log ll){
		this.source = source;
		this.ll = ll;
		names = new ArrayList<String>();
		links = new ArrayList<String>();
		list_index = 0;
	}
	
	public void parse_links(){
		ll.write(2, "Parsing Links\r\n");
		String str = StrOps.getDilineatedSubstring(source, "twikiContentHeader", 1, false);
		str = StrOps.getDilineatedSubstring(str, "twikiContentFooter", 0, false);
		str = StrOps.getDilineatedSubstring(str, "<ul>", 1, false);
		str = StrOps.getDilineatedSubstring(str, "</ul>", 0, false);
		str = StrOps.deleteAllInstances(str, "<li>");
		str = StrOps.deleteAllInstances(str, "</li>");
		str = StrOps.deleteAllInstances(str, "</a>");
		str = StrOps.deleteAllInstances(str, "<a ");
		str = StrOps.deleteAllInstances(str, "</span>");
		str = StrOps.deleteAllInstances(str, "<span ");
		print("str.txt",str);
		
		try{
			BufferedReader br = new BufferedReader(new FileReader("str.txt"));
			String line = br.readLine();
			while(line != null){
				if(!line.equals("")){
					boolean page_exists = true;
					String link = "";
					if(!StrOps.patternMatch(line, "*class*")){
						ll.write(1,"Links do not follow template. Cannot find \'class\' " + line + "\r\n");
						System.exit(-1);
					}
					String class_type = StrOps.getDilineatedSubstring(line, "\"", 1, false);
					if(class_type.equals("twikiLink")){
						if(!StrOps.patternMatch(line, "*href*")){
							ll.write(1,"Links do not follow template. Cannot find \'href\' " + line + "\r\n");
							System.exit(-1);
						}
						link = DEFINE.SITE_BASE + StrOps.getDilineatedSubstring(line, "\"", 3, false);
					} else if(class_type.equals("twikiNewLink")){
						page_exists = false;
					} else{
						ll.write(1, "class type not understood " + class_type + "\r\n");
						System.exit(-1);
					}
					
					String name = StrOps.getDilineatedSubstring(line, ">", 0, true);
					
					if(!StrOps.patternMatch(name, "*" + DEFINE.ASSIGNMENT_NAME + "*")){
						ll.write(1,"Links do not follow template. Cannot find " + DEFINE.ASSIGNMENT_NAME + " " + line + "\r\n");
						System.exit(-1);
					}
					name = StrOps.deleteAllInstances(name, DEFINE.ASSIGNMENT_NAME);
					
					names.add(name);
					if(page_exists)
						links.add(link);
					else
						links.add("");
				}
				line = br.readLine();
			}
			br.close();
		} catch(IOException e){
			ll.write(1,"Error with reading str.txt\r\n");
			System.exit(-1);
		}
	}
	
	public void print_names_and_links(String file){
		ll.write(2, "Writing to " + file + "\r\n");
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
			for(int ii = 0; ii < names.size(); ii++){
				bw.write(names.get(ii) + " " + links.get(ii)+"\r\n");
			}
			bw.close();
		} catch (IOException e) {
			ll.write(1,"Write failed!");
		}
	}
	
	public void print(String file, String str){
		ll.write(2, "Writing to " + file + "\r\n");
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
			bw.write(str);
			bw.close();
		} catch (IOException e) {
			ll.write(1,"Write failed!");
		}
	}
	
	public ArrayList<String> get_next_link(){
		if(list_index >= names.size())
			return new ArrayList<String>();
		ArrayList<String> list = new ArrayList<String>();
		list.add(names.get(list_index));
		list.add(links.get(list_index));
		list_index++;
		return list;
	}
}
