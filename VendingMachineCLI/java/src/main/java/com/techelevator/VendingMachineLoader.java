package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class VendingMachineLoader {
	
	private File inputFile;
	private Map stockedItemMap = new HashMap<String, Item>();

	public VendingMachineLoader() throws FileNotFoundException{
		this.inputFile = getFileFromSystem();
		generateStockedItemMap();
	}
	
	
	public Map getStockedItemMap() {
		return stockedItemMap;
	}
	
	
	public void generateStockedItemMap() throws FileNotFoundException {
		
		
		try(Scanner fileScanner = new Scanner(this.inputFile)){
			String [] itemLine;
			while(fileScanner.hasNextLine()) {
				
				itemLine = fileScanner.nextLine().split("\\|");
				
				//System.out.println(itemLine[1]); //test console out to verify we are actually populating the right fields
				
				String key = itemLine[0];
				String name = itemLine[1];
				BigDecimal price = new BigDecimal(itemLine[2]);
				String itemType = itemLine[3];
				
				Item itemToAdd = new StockedItem(name,price,itemType);
				
				this.stockedItemMap.put(key, itemToAdd);
				
				}

			}


		}
		

	public File getFileFromSystem () {
		
		String path = "vendingmachine.csv";
		
		File systemInputFile = new File(path);
		if(systemInputFile.exists() == false) { // checks for the existence of a file
			System.out.println(path+" does not exist");
			System.exit(1); // Ends the program
		} else if(systemInputFile.isFile() == false) {
			System.out.println(path+" is not a file");
			System.exit(1); // Ends the program
		}
				
		return systemInputFile;
		
	}
	

		
	}
	



