package parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import logging.Log;
import string_operations.StrOps;
import basics.DEFINE;

public class LinkParser {
	String source;
	Log ll;
	
	public LinkParser(String source, Log ll){
		this.source = source;
		this.ll = ll;
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
				if(!StrOps.patternMatch(line, "class*")){
					ll.write(1,"Links do not follow template. Cannot find \'class\' " + line + "\r\n");
					System.exit(-1);
				}
				
				
				line = br.readLine();
				
			}
		} catch(IOException e){
			ll.write(1,"Error with reading str.txt\r\n");
			System.exit(-1);
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
}
