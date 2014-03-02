package parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


import string_operations.StrOps;
import logging.Log;
import data_structure.FastStudent;

public class ExcelParser {
	private Log ll;
	private ArrayList<ArrayList<String>> data_matrix;
	
	private ArrayList<Integer> questions = new ArrayList<Integer>();
	private ArrayList<Boolean> parts_exist = new ArrayList<Boolean>();
	private ArrayList<Integer> q_location = new ArrayList<Integer>();
	private ArrayList<Integer> q_maximum = new ArrayList<Integer>(); 
	private ArrayList<ArrayList<String>> parts = new ArrayList<ArrayList<String>>();
	private ArrayList<ArrayList<Integer>> p_location = new ArrayList<ArrayList<Integer>>();
	private ArrayList<ArrayList<Integer>> p_maximum = new ArrayList<ArrayList<Integer>>(); 
	
	private ArrayList<String>students = new ArrayList<String>(); 
	
	public ExcelParser(Log ll){
		this.ll = ll;
		ll.write(2,"Starting ExcelParser\r\n");
	}
	
	public ArrayList<FastStudent> getListOfStudents(){
		ArrayList<FastStudent>list = new ArrayList<FastStudent>();
		for(int ii = 2; ii < students.size()+2; ii++){
			FastStudent stu = new FastStudent(students.get(ii-2));
			stu.loadQuestions(questions, parts_exist, q_maximum, p_maximum);
			
			//first get the overall comment
			stu.overall_comment = data_matrix.get(ii).get(1);
			
			//get the grades
			for(int jj = 0; jj < questions.size(); jj++){
				if(parts_exist.get(jj)){
					for(int kk = 0; kk < parts.get(jj).size(); kk++){
						float value = 0;
						if(data_matrix.get(ii).get(p_location.get(jj).get(kk)).equals(""))
							value = 0;
						else
							value = Float.parseFloat(data_matrix.get(ii).get(p_location.get(jj).get(kk)));
						stu.p_grades.get(jj).set(kk, value);
						stu.p_comments.get(jj).set(kk, data_matrix.get(ii).get(p_location.get(jj).get(kk)+1));
					}
				} else{
					try{
						if(data_matrix.get(ii).get(q_location.get(jj)).equals(""))
							stu.q_grades.set(jj, (float) 0);
						else
							stu.q_grades.set(jj, Float.parseFloat(data_matrix.get(ii).get(q_location.get(jj))));
					} catch(Exception e){
						System.out.println("Error");
					}
					stu.q_comments.set(jj, data_matrix.get(ii).get(q_location.get(jj)+1));
				}
			}
			
			//get total
			stu.calculateTotal();
			list.add(stu);
		}
		return list;
	}
	
	public void getExcelFile(String file, String rows, String columns){
		data_matrix = new ArrayList<ArrayList<String>>();
		
		int start_row_index = Integer.parseInt(StrOps.getDilineatedSubstring(rows, "_", 0, false)) - 1;
		int end_row_index = Integer.parseInt(StrOps.getDilineatedSubstring(rows, "_", 1, false)) - 1;
		int start_col_index = (StrOps.getDilineatedSubstring(columns, "_", 0, false).charAt(0)) - 'A';
		int end_col_index = (StrOps.getDilineatedSubstring(columns, "_", 1, false).charAt(0)) - 'A';
		//System.out.println("start_r)ow_index = " + start_row_index + " end_row_index = " + end_row_index + " start_col_index = " + start_col_index + " end_col_index = " + end_col_index);
		
		try{
			FileInputStream input = new FileInputStream(file);
			POIFSFileSystem fs = new POIFSFileSystem(input);
			HSSFWorkbook workbook = new HSSFWorkbook(fs);
			HSSFSheet sheet = workbook.getSheetAt(0);
			for(int ii = start_row_index; ii <= end_row_index; ii++){
				HSSFRow row = sheet.getRow(ii);
				ArrayList<String> col = new ArrayList<String>();
				for(int jj = start_col_index; jj <= end_col_index; jj++){
					HSSFCell cell = row.getCell(jj);
					if(cell == null)
						col.add("");
					else{
						try{
							col.add(cell.toString());
						} catch(NullPointerException e){
							System.out.println("NUll");
							System.exit(-1);
						}
					}
				}
				data_matrix.add(col);
			}
		} catch(FileNotFoundException e){
			ll.write(1, "File not found");
			System.exit(-1);
		} catch (IOException e) {
			ll.write(1, "IOException");
			System.exit(-1);
		}
	}
	
	public void getQuestions(){		
		for(int ii = 0; ii < data_matrix.get(0).size(); ii++){
			String str = data_matrix.get(0).get(ii);
			if(str.length() != 0){
				if(str.charAt(0) >= '0' && str.charAt(0) <= '9'){
					String question_str = "";
					String part_str = "";
					boolean part = false;
					for(int jj = 0; jj < str.length(); jj++){
						if(str.charAt(jj) == '.'){
							jj++;
							while(jj < str.length() && (str.charAt(jj) >= '0' && str.charAt(jj) <= '9'))
								jj++;
							if(jj != str.length())
								part = true;
						}
						else if(part == true)
							part_str += str.charAt(jj);
						else 
							question_str += str.charAt(jj);
					}
					int q = Integer.parseInt(question_str);
					if(part){
						boolean found = false;
						int jj;
						for(jj = 0; jj < questions.size(); jj++){
							if(questions.get(jj) == q){
								found = true;
								break;
							}
						}
						if(found){
							parts.get(jj).add(part_str);
							p_location.get(jj).add(ii);
							p_maximum.get(jj).add(0);
						}
						else{
							questions.add(q);
							parts.add(new ArrayList<String>());
							parts.get(parts.size()-1).add(part_str);
							parts_exist.add(true);
							p_location.add(new ArrayList<Integer>());
							p_location.get(p_location.size()-1).add(ii);
							p_maximum.add(new ArrayList<Integer>());
							p_maximum.get(p_maximum.size()-1).add(0);
						}								
					} else{
						questions.add(q);
						q_location.add(ii);
						parts_exist.add(false);
						parts.add(new ArrayList<String>());
						q_maximum.add(0);
					}
				}
			}
		}
	}
	
	public void getMaxScores(){
		for(int ii = 0; ii < questions.size(); ii++){
			if(parts_exist.get(ii)){
				for(int jj = 0; jj < parts.get(ii).size(); jj++)
					p_maximum.get(ii).set(jj, (int)Float.parseFloat(data_matrix.get(1).get(p_location.get(ii).get(jj))));
			} else
				q_maximum.set(ii, (int)Float.parseFloat(data_matrix.get(1).get(q_location.get(ii))));
		}
	}
	
	public void printQuestionsAndParts(){
		System.out.println("\nQuestions:");
		for(int ii = 0; ii < questions.size(); ii++){
			if(parts_exist.get(ii)){
				for(int jj = 0; jj < parts.get(ii).size(); jj++)
					System.out.println(Integer.toString(questions.get(ii)) + parts.get(ii).get(jj));
			}
			else {
				System.out.println(Integer.toString(questions.get(ii)) + " at column " + Integer.toString(q_location.get(ii)) + " -> " + Integer.toString(q_maximum.get(ii)));
			}
		}
		System.out.println("\n");
	}
	
	public ArrayList<String> getStudents(){
		for(int ii = 2; ii < data_matrix.size(); ii++){
			students.add(data_matrix.get(ii).get(0));
			//System.out.println(data_matrix.get(ii).get(0));
		}	
		return students;
	}
	
	public void printCellData(){
		for(int ii = 0; ii < data_matrix.size(); ii++){
			for(int jj = 0; jj < data_matrix.get(ii).size(); jj++)
				System.out.print(data_matrix.get(ii).get(jj) + "\t");
			System.out.println("");
		}
	}
	
	
}
