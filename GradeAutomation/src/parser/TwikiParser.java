package parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import logging.Log;
import string_operations.StrOps;
import file_operations.FileOps;

public class TwikiParser {
	String twiki_file;
	Log ll;
	boolean save_files;
	boolean load_from_save;
	String save_location;
	String MATCH_CODE;
	
	public TwikiParser(Log ll, boolean save_files, String save_location, String MATCH_CODE, boolean load_from_save){
		twiki_file = "";
		this.ll = ll;
		this.save_files = save_files;
		this.save_location = save_location;
		this.MATCH_CODE = MATCH_CODE;
		this.load_from_save = load_from_save;
	}
	
	public void loadTwikiFile(String file_location){
		try{
			if(!load_from_save){
				ll.write(2, "Loading file " + file_location);
				twiki_file = "";
				BufferedReader br = new BufferedReader(new FileReader(file_location));
				String line = br.readLine();
				while(!StrOps.patternMatch(line, "*TWIKIFILEEND*")){
					twiki_file += line;
					twiki_file += "\n";
					line = br.readLine();
				}
				br.close();
			} else{
				String name = StrOps.getDilineatedSubstring(file_location, "\\", 0, true);
				ll.write(2, "Loading file " + save_location + "\\" + name);
				twiki_file = "";
				BufferedReader br = new BufferedReader(new FileReader(save_location + "\\" + name));
				String line = br.readLine();
				while(line != null){
					twiki_file += line;
					twiki_file += "\n";
					line = br.readLine();
				}
				br.close();
			}
			if(save_files && !load_from_save){
				String name = StrOps.getDilineatedSubstring(file_location, "\\", 0, true);
				if(FileOps.fileExists(save_location + "\\" + name)){
					ll.write(1, "Trying to overwrite save file " + save_location + "\\" + name);
					System.exit(-1);
				}					
				BufferedWriter bw = new BufferedWriter(new FileWriter(save_location + "\\" + name, false));
				bw.write(twiki_file);
				bw.close();
			}
		} catch(IOException e){
			ll.write(1, "Failed to load " + file_location);
			System.exit(-1);
		}
	}
	
	public void printTwikiFile(){
		System.out.println(twiki_file);
	}
	
	public void insertOverallGrade(String overall_grade){
		String out = "\n\n <h1 style=\"color:blue;\">TOTAL GRADE</h1>\n";
		out += "\n <p style=\"color:blue;font-size:15px;\"><b> STAFF GRADE:\n";
		out += overall_grade;
		out += "\n\n</b></p>\n" + MATCH_CODE + "\n\n";
		int index = StrOps.findPattern(twiki_file, "%TOC%");
		if(index == -1){
			twiki_file = out + twiki_file;
		}
		else{
			index += 5;
			twiki_file = twiki_file.substring(0,index) + out + twiki_file.substring(index);
		}
	}
	
	public void insertGradeOutput(ArrayList<String> output){
		boolean found = true;
		for(int ii = 0; ii < output.size(); ii++){
			if(!StrOps.patternMatch(twiki_file, "*---+++Problem " + Integer.toString(ii+1) + " grade:*")){
				found = false;
				break;
			}
		}
		if(!found){
			int index = StrOps.findPattern(twiki_file, MATCH_CODE);
			index += MATCH_CODE.length();
			String twiki_sub = twiki_file.substring(0,index);
			twiki_sub += "\n\n<p style=\"color:blue;font-size:15px\"><b>\n\n";
			for(int ii = 0; ii < output.size(); ii++){
				twiki_sub += "STAFF GRADE PROBLEM " +Integer.toString(ii+1) + "\n";
				twiki_sub += output.get(ii);
				twiki_sub += "\n\n";
			}
			twiki_sub += "</b></p>\n";
			twiki_sub += twiki_file.substring(index);
			twiki_file = twiki_sub;			
		} else{
			for(int ii = 0; ii < output.size(); ii++){
				String q = "---+++Problem " + Integer.toString(ii+1) + " grade:";
				int index = StrOps.findPattern(twiki_file, q);
				index += q.length();
				String twiki_sub = twiki_file.substring(0,index);
				twiki_sub += "\n\n<p style=\"color:blue;font-size:15px\"><b>\n\n";
				twiki_sub += output.get(ii);
				twiki_sub += "</b></p>\n";
				twiki_sub += twiki_file.substring(index);
				twiki_file = twiki_sub;
			}
		}
	}
	
	public String returnTwikiFile(){
		return twiki_file;
	}
}
