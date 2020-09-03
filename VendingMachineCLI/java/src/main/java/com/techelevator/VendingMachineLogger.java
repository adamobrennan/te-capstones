package com.techelevator;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class VendingMachineLogger implements Closeable  {
	
	private File logFile;
	private PrintWriter logWriter;

	public VendingMachineLogger(String logFilePath) throws FileNotFoundException {
		
		this.logFile = new File(logFilePath);
		
		if(this.logFile.exists()) {
			
			try{
				this.logWriter = new PrintWriter(new FileWriter(this.logFile,true));	
				
			} catch (Exception e) {	
				e.getMessage();
			}
	
		} else {
			this.logWriter = new PrintWriter(logFile);
		}
		


	}
	
	public File getLogFile() {
		return this.logFile; 
	}
	
	
	public void write(String logMessage) {
		
		try {
			logWriter.println(logMessage);
			logWriter.flush();
			
		} catch(Exception e) {
			e.getMessage();
			throw e;
		}
		
	}

	@Override
	public void close() throws IOException {

		try{
			logWriter.close();
		}
		catch(Exception e) {
			e.getMessage();
		}
	}

}
