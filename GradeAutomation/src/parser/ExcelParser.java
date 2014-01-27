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

public class ExcelParser {
	private Log ll;
	private ArrayList<ArrayList<String>> data_matrix;
	
	public ExcelParser(Log ll){
		this.ll = ll;
		ll.write(2,"Starting ExcelParser\r\n");
	}
	
	public void getExcelFile(String file, String rows, String columns){
		data_matrix = new ArrayList<ArrayList<String>>();
		
		int start_row_index = Integer.parseInt(StrOps.getDilineatedSubstring(rows, "_", 0, false)) - 1;
		int end_row_index = Integer.parseInt(StrOps.getDilineatedSubstring(rows, "_", 1, false)) - 1;
		int start_col_index = (StrOps.getDilineatedSubstring(columns, "_", 0, false).charAt(0)) - 'A';
		int end_col_index = (StrOps.getDilineatedSubstring(columns, "_", 1, false).charAt(0)) - 'A';
		//System.out.println("start_row_index = " + start_row_index + " end_row_index = " + end_row_index + " start_col_index = " + start_col_index + " end_col_index = " + end_col_index);
		
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
					col.add(cell.toString());
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
	
	public void get_grade_data(){
		ArrayList<String> l1 = data_matrix.get(0);
		ArrayList<String> l2 = data_matrix.get(1);
		
		for(int ii = l1.size()-1; ii >= 0; ii--){
			if(l1.get(ii).equals("Person") || l1.get(ii).equals("Comments")){
				l1.remove(ii);
			}
		}
	}
	
	public void printCellData(){
		for(int ii = 0; ii < data_matrix.size(); ii++){
			for(int jj = 0; jj < data_matrix.get(ii).size(); jj++)
				System.out.print(data_matrix.get(ii).get(jj) + "\t");
			System.out.println("");
		}
	}
	
	
}
