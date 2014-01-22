package control_structure;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import logging.Log;
import basics.DEFINE;
import web.Client;
import parser.LinkParser;

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
		ll.write(2, "Starting Client.\r\n");
		Client cc = new Client(DEFINE.BASE_URL, ll);
		ll.write(2, "Client ready. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");
		
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Dealing with login");
		if(cc.islogin())
			cc.login(DEFINE.USERNAME, DEFINE.PASSWORD);
		cc.printPage("page.txt");
		ll.write(2, "Login complete. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");
		
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Starting Link Parser.\r\n");
		LinkParser lp = new LinkParser(cc.getPageSource(), ll);
		ll.write(2, "Link Parser ready. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");
		
		
		
		/*
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Starting Exec.\r\n");
		Exec ee = new Exec(ll);
		ll.write(2, "Exec ready. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");
		
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Starting Client.\r\n");
		String test_URL = Def.START_URL;
		Client cc = new Client(test_URL, ll);
		ll.write(2, "Client ready. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");
		
		time_elapsed = System.currentTimeMillis();
		ll.write(2, "Operation begin");
		while(cc.getImage())
			ee.convert_and_compound();
		ll.write(2, "Operation complete. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.\r\n");
		*/

		ll.write(2, "All operations complete at " + dateFormat.format(new Date()) + ". Took " + (System.currentTimeMillis() - time_start) + "ms.\r\n");
		ll.write(2, "Performing cleanup.\r\n\r\n");
		time_elapsed = System.currentTimeMillis();
		
		ll.close();
		
		System.out.println("Cleanup complete. Took " + (System.currentTimeMillis() - time_elapsed) + "ms.");
		System.out.println("All operations complete.");
	}
}
