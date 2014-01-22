package web;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import logging.Log;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


//import basics.DEFINE;
import string_operations.StrOps;


public class Client {
	String base_URL;
	Log ll;
	WebDriver web;
	int image_number;
	
	public Client(String base_URL, Log ll){
		this.base_URL = base_URL;
		this.ll = ll;
		
		ll.write(2, "Starting web driver\r\n");
		web = new FirefoxDriver();
		
		ll.write(2, "Loading page " + base_URL + "\r\n");
		web.get(base_URL);	
	}
	
	public void printPage(String file){
		try{
			ll.write(2, "Writing page source to file.\r\n");
			String text = web.getPageSource();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file,false));
			bw.write(text);
			bw.close();			
		}catch(IOException e){
			ll.write(1, "Failed to open " + file + ". Writing page source failed.\r\n");
		}
	}
	
	public void close(){
		ll.write(2, "Closing driver");
		web.close();
	}
	
	public boolean islogin(){
		ll.write(2, "Checking if logged in");
		String title = web.getTitle();
		if(StrOps.patternMatch(title, "*TWiki login*"))
			return true;
		return false;
	}
	
	public void login(String username, String password){
		ll.write(2, "Logging in");
		ll.write(2, "Getting username");
        WebElement query = web.findElement(By.name("username"));
        query.sendKeys(username);

        ll.write(2, "Getting password");
        WebElement pass = web.findElement(By.name("password"));
        pass.sendKeys(password);
        
        pass.submit();
	}
	
	public String getPageSource(){
		return web.getPageSource();
	}
}
