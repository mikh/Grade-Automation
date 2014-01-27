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
import robot.Robot_controller;


public class Control {
	public static void main(String[] args){
		System.out.println("Starting Grade Automation Engine.");
		System.out.println("Starting logger.");
		Log ll = new Log(DEFINE.LOGGING_LEVEL, DEFINE.LOG_FILE_PATH);
		ll.write(2, "\r\nStarting Grade Automation Engine v0.0\r\n");
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		ll.write(2, "Starting at " + dateFormat.format(new Date()) + "\r\n");
		long time_start = System.currentTimeMillis(), time_elapsed;
		
		/*
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Starting Client.\r\n");
		Client cc = new Client(DEFINE.BASE_URL, ll);
		ll.write(2, "Client ready. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");
		
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Dealing with login\r\n");
		if(cc.islogin())
			cc.login(DEFINE.USERNAME, DEFINE.PASSWORD);
		ll.write(2, "Login complete. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");
		
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Starting Link Parser.\r\n");
		LinkParser lp = new LinkParser(cc.getPageSource(), ll);
		ll.write(2, "Link Parser ready. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");
				
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Parsing Links.\r\n");
		lp.parse_links();
		ll.write(2, "Parsing complete. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");
		
		
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Acquiring pages.\r\n");
		ArrayList<String> list = lp.get_next_link();
		while(list.size() > 0){
			if(!list.get(1).equals("")){
				Tools.delay(1000);
				System.out.println(list.get(1));
				cc.getPage(list.get(1));
				Tools.delay(2000);
				ll.write(2, "Going into raw edit\r\n");
				Robot_controller.type("/shift//alt/e");
				Tools.delay(1000);
			
				try {
					Robot rb = new Robot();
					rb.keyPress(KeyEvent.VK_ENTER);
					Tools.delay(50);
					rb.keyRelease(KeyEvent.VK_ENTER);
				} catch (AWTException e) {
					System.exit(-1);
				}
				
				Tools.delay(1000);
				Robot_controller.type("/ctrl/a");
				Tools.delay(1000);
				Robot_controller.type("/ctrl/c");
				Tools.delay(1000);
				String out = Tools.clipboard_to_string();
				Tools.writeToFile(DEFINE.OUTPUT_FILE_PATH + "\\" + list.get(0) + ".txt", out, false);
			}
			list = lp.get_next_link();
		}
		ll.write(2, "Pages Acquired. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");
		*/
		
		ll.write(2, "Loading excel data\r\n");
		ExcelParser ep = new ExcelParser(ll);
		ep.getExcelFile(DEFINE.EXCEL_FILE_PATH, DEFINE.ROWS, DEFINE.COLUMNS);
		

		ll.write(2, "All operations complete at " + dateFormat.format(new Date()) + ". Took " + (System.currentTimeMillis() - time_start) + "ms.\r\n");
		ll.write(2, "Performing cleanup.\r\n\r\n");
		time_elapsed = System.currentTimeMillis();
		
		//cc.close();
		ll.close();
		
		System.out.println("Cleanup complete. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.");
		System.out.println("All operations complete.");
	}
}
