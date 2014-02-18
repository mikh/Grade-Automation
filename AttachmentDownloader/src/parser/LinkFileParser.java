package parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import string_operations.StrOps;

public class LinkFileParser {
	public ArrayList<String> names, links;
	public LinkFileParser(){
		names = new ArrayList<String>();
		links = new ArrayList<String>();
	}
	
	public void parse(String path){
		try{
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line = br.readLine();
			while(line != null){
				String name = StrOps.getDilineatedSubstring(line, " ", 0, false);
				names.add(name);
				links.add(line.substring(name.length()+1));
				line = br.readLine();
			}
			br.close();
		} catch(IOException e){
			System.out.println("Error with parsing " + path);
		}
	}
}
