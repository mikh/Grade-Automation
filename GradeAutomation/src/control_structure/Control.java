package control_structure;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import logging.Log;
import basics.DEFINE;
import basics.Tools;
import web.Client;
import parser.LinkParser;
import parser.ExcelParser;
import parser.TwikiParser;
import robot.Robot_controller;
import data_structure.FastStudent;


public class Control {
	public static void main(String[] args){
		System.out.println("Starting Grade Automation Engine.");
		System.out.println("Starting logger.");
		Log ll = new Log(DEFINE.LOGGING_LEVEL, DEFINE.LOG_FILE_PATH);
		ll.write(2, "\r\nStarting Grade Automation Engine v0.0\r\n");
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		ll.write(2, "Starting at " + dateFormat.format(new Date()) + "\r\n");
		long time_start = System.currentTimeMillis(), time_elapsed;
		
		
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Creating Student Structure.\r\n");
		ArrayList<FastStudent> student_list = new ArrayList<FastStudent>();
		ll.write(2, "Student structure ready. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");
		
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Loading excel data\r\n");
		ExcelParser ep = new ExcelParser(ll);
		ep.getExcelFile(DEFINE.EXCEL_FILE_PATH, DEFINE.ROWS, DEFINE.COLUMNS);
		ep.getQuestions();
		ep.getMaxScores();
		ep.getStudents();
		ll.write(2, "Excel data ready. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");
		
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Setting up Student data\r\n");
		student_list = ep.getListOfStudents();
		for(int ii = 0; ii < student_list.size(); ii++)
			student_list.get(ii).createOutput();
		ll.write(2, "Student data ready. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");
		

		
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Starting Client.\r\n");
		Client cc = new Client(DEFINE.BASE_URL, ll);
		ll.write(2, "Client ready. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");
		
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Dealing with login\r\n");
		while(cc.islogin())
			cc.login(DEFINE.USERNAME, DEFINE.PASSWORD);
		ll.write(2, "Login complete. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");
		
		
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Starting Link Parser.\r\n");
		LinkParser lp = new LinkParser(cc.getPageSource(), ll);
		ll.write(2, "Link Parser ready. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");
				
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Parsing Links.\r\n");
		lp.parse_links();
		lp.print_names_and_links("C:\\Users\\Mikhail\\Desktop\\Dropbox\\EC504 Grading\\Solutions\\HomeworkOne\\links.txt");
		ll.write(2, "Parsing complete. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");
		
		
		
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Acquiring pages.\r\n");
		if(!DEFINE.LOAD_FROM_SAVE_FILE){
			if(DEFINE.GRADE_ALL){
				for(int ii = 0; ii < student_list.size(); ii++){
					ArrayList<String> list = lp.get_link(student_list.get(ii).name);
					if(list.size() == 0){
						ll.write(1, "Cannot find " + student_list.get(ii).name);
						System.exit(-1);
					}
					student_list.get(ii).link = list.get(1);
					Tools.delay(1000);
					cc.getPage(list.get(1));
					Tools.delay(2000);
					ll.write(2, "Going into raw edit\r\n");
					Robot_controller.type("/shift//alt/e/d1000//enter:1000//d1000//ctrl/a/d1000//ctrl/c/d1000/");
					String out = Tools.clipboard_to_string();
					Tools.writeToFile(DEFINE.OUTPUT_FILE_PATH + "\\" + list.get(0) + ".txt", out, false);
					student_list.get(ii).file_location = DEFINE.OUTPUT_FILE_PATH + "\\" + list.get(0) + ".txt";			
				}
			} else{
				for(int ii = 0; ii < DEFINE.PEOPLE_TO_GRADE.size(); ii++){
					int index = -1;
					for(int jj = 0; jj < student_list.size(); jj++){
						if(student_list.get(jj).name.equals(DEFINE.PEOPLE_TO_GRADE.get(ii))){
							index = jj;
							break;
						}
					}
					if(index == -1){
						ll.write(1, "Cannot find " + DEFINE.PEOPLE_TO_GRADE.get(ii));
						System.exit(-1);
					}
					ArrayList<String> list = lp.get_link(student_list.get(index).name);
					if(list.size() == 0){
						ll.write(1, "Cannot find " + student_list.get(index).name);
						System.exit(-1);
					}
					student_list.get(index).link = list.get(1);
					Tools.delay(1000);
					cc.getPage(list.get(1));
					Tools.delay(2000);
					ll.write(2, "Going into raw edit\r\n");
					Robot_controller.type("/shift//alt/e/d1000//enter:1000//d1000//ctrl/a/d1000//ctrl/c/d1000/");
					String out = Tools.clipboard_to_string() + "\r\nTWIKIFILEEND\r\n";
					Tools.writeToFile(DEFINE.OUTPUT_FILE_PATH + "\\" + list.get(0) + ".txt", out, false);
					student_list.get(index).file_location = DEFINE.OUTPUT_FILE_PATH + "\\" + list.get(0) + ".txt";
				}
			}
		} else{
			if(DEFINE.GRADE_ALL){
				for(int ii = 0; ii < student_list.size(); ii++){
					ArrayList<String> list = lp.get_link(student_list.get(ii).name);
					if(list.size() == 0){
						ll.write(1, "Cannot find " + student_list.get(ii).name);
						System.exit(-1);
					}
					student_list.get(ii).link = list.get(1);
					student_list.get(ii).file_location = DEFINE.OUTPUT_FILE_PATH + "\\" + list.get(0) + ".txt";			
				}
			} else{
				for(int ii = 0; ii < DEFINE.PEOPLE_TO_GRADE.size(); ii++){
					int index = -1;
					for(int jj = 0; jj < student_list.size(); jj++){
						if(student_list.get(jj).name.equals(DEFINE.PEOPLE_TO_GRADE.get(ii))){
							index = jj;
							break;
						}
					}
					if(index == -1){
						ll.write(1, "Cannot find " + DEFINE.PEOPLE_TO_GRADE.get(ii));
						System.exit(-1);
					}
					ArrayList<String> list = lp.get_link(student_list.get(index).name);
					if(list.size() == 0){
						ll.write(1, "Cannot find " + student_list.get(index).name);
						System.exit(-1);
					}
					student_list.get(index).link = list.get(1);
					student_list.get(index).file_location = DEFINE.OUTPUT_FILE_PATH + "\\" + list.get(0) + ".txt";
				}
			}
		}
		ll.write(2, "Pages Acquired. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");
		
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Writing data to pages.\r\n");
		TwikiParser tp = new TwikiParser(ll, DEFINE.SAVE_FILES, DEFINE.SAVE_TWIKI_CODE_PATH, DEFINE.MATCH_CODE, DEFINE.LOAD_FROM_SAVE_FILE);
		if(!DEFINE.GRADE_ALL){
			for(int ii = 0; ii < DEFINE.PEOPLE_TO_GRADE.size(); ii++){
				int index = -1;
				for(int jj = 0; jj < student_list.size(); jj++){
					if(student_list.get(jj).name.equals(DEFINE.PEOPLE_TO_GRADE.get(ii))){
						index = jj;
						break;
					}
				}
				if(index == -1){
					ll.write(1, "Cannot find " + DEFINE.PEOPLE_TO_GRADE.get(ii));
					System.exit(-1);
				}
				tp.loadTwikiFile(student_list.get(index).file_location);
				tp.insertOverallGrade(student_list.get(index).overall_output);
				tp.insertGradeOutput(student_list.get(index).q_output);
				String out = tp.returnTwikiFile();
				Tools.delay(1000);
				cc.getPage(student_list.get(index).link);
				Tools.delay(2000);
				ll.write(2, "Going into raw edit\r\n");
				Tools.string_to_clipboard(out);
				Robot_controller.type("/shift//alt/e/d1000//enter:1000//d1000//ctrl/a/d1000//ctrl/v/d1000//shift//alt/s/d1000/");
			}
		} else{
			for(int ii = 0; ii < student_list.size(); ii++){
				tp.loadTwikiFile(student_list.get(ii).file_location);
				tp.insertOverallGrade(student_list.get(ii).overall_output);
				tp.insertGradeOutput(student_list.get(ii).q_output);
				String out = tp.returnTwikiFile();
				Tools.delay(1000);
				cc.getPage(student_list.get(ii).link);
				Tools.delay(2000);
				ll.write(2, "Going into raw edit\r\n");
				Tools.string_to_clipboard(out);
				Robot_controller.type("/shift//alt/e/d1000//enter:1000//d1000//ctrl/a/d1000//ctrl/v/d1000//shift//alt/s/d1000/");
			}
		}
		ll.write(2, "Data written. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");

		

		ll.write(2, "All operations complete at " + dateFormat.format(new Date()) + ". Took " + (System.currentTimeMillis() - time_start) + "ms.\r\n");
		ll.write(2, "Performing cleanup.\r\n\r\n");
		time_elapsed = System.currentTimeMillis();
		
		//cc.close();
		ll.close();
		
		System.out.println("Cleanup complete. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.");
		System.out.println("All operations complete.");
	}
}
