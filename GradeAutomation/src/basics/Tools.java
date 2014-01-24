package basics;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Tools {
	public static String clipboard_to_string(){
		try{
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Clipboard clipboard = toolkit.getSystemClipboard();
			return (String) clipboard.getData(DataFlavor.stringFlavor);
		} catch(IOException e){
			System.out.println("[Error] Clipboard failure.");
			System.exit(-1);
		} catch (UnsupportedFlavorException e) {
			System.out.println("[Error] Unsupported Data Flavor");
			System.exit(-1);
		}
		return null;
	}
	
	public static void delay(int milliseconds){
		long start_time = System.currentTimeMillis();
		while(System.currentTimeMillis() - start_time < milliseconds);
	}
	
	public static void writeToFile(String filename, String content, boolean append){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename,append));
			bw.write(content);
			bw.close();
		} catch(IOException e){
			System.out.println("[Error] Write to file failure.");
			System.exit(-1);
		}
	}
}
