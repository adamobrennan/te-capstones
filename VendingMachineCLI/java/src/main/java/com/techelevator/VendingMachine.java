package com.techelevator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class VendingMachine {
	private Map<String, StockedItem> stockedItems;
//	private SalesReport currentSalesReport; to do later
	private BigDecimal currentMoneyBalance;
	private VendingMachineLoader loader;
	private VendingMachineLogger log;
	private DateTimeFormatter timeFormatter;
	private DateTimeFormatter dateFormatter;
	private DateTimeFormatter salesReportDateFormatter;
	private DateTimeFormatter salesReportTimeFormatter;
	private VendingMachineLogger salesLog;
	private BigDecimal salesReportTotal;
	
	public VendingMachine() throws FileNotFoundException {

		this.currentMoneyBalance = new BigDecimal("0.00");
		loadVendingMachine();
		this.stockedItems = loader.getStockedItemMap();
		this.salesLog = new VendingMachineLogger( "salesLog.txt"); 
		this.log = new VendingMachineLogger("log.txt");
		timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.US);
		dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		salesReportDateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
		salesReportTimeFormatter = DateTimeFormatter.ofPattern("hh-mm-ss_a", Locale.US);
		

	}
	
	
	
	
	

	public void loadVendingMachine() {

		try {
			this.loader = new VendingMachineLoader();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getItemCodeFromUserAndDispense() {
		Scanner userInput = new Scanner(System.in);
		String itemCode = userInput.nextLine().toUpperCase();
		if (stockedItems.containsKey(itemCode)) {
			dispense(itemCode);

		} else {
			System.out.println("Invalid Entry");
		}
	}

	public boolean dispense(String code) {
		
		String logEntry = LocalDate.now().format(dateFormatter) + " " + LocalTime.now().format(timeFormatter) + " ";

		StockedItem selectedItem = this.stockedItems.get(code);
		if (!selectedItem.inStock()) {
			System.out.println("Item is out of stock");
			return false;
		}
		if (currentMoneyBalance.compareTo(selectedItem.getPrice()) == -1) {
			System.out.println("Insufficient balance");
			return false;
		}
		
		logEntry = logEntry + selectedItem.getName() + " " + code + " $" + currentMoneyBalance.toString();

		currentMoneyBalance = currentMoneyBalance.subtract(selectedItem.getPrice());
		selectedItem.decreaseStockByOne();

		System.out.println("Item dispensed: " + selectedItem.getName() + " at a cost of " + selectedItem.getPrice()
				+ " you have  $" + currentMoneyBalance + " remaining.");
		System.out.println(selectedItem.getDispenseMessage());
		
		logEntry = logEntry + " $" + currentMoneyBalance.toString();
		log.write(logEntry);
		salesLog.write(selectedItem.getName()+ " " + selectedItem.getPrice());
		return true;

	}
		
	public Map<String, Integer> processSalesReport() throws FileNotFoundException {
		Scanner salesLogScanner = new Scanner(salesLog.getLogFile());
		Map< String, Integer> salesReportMap = new HashMap<>();
		List <BigDecimal> pricesList = new ArrayList<>();
		
		while ( salesLogScanner.hasNextLine()) {
				String [] salesLine = salesLogScanner.nextLine().split(" ");
				
				BigDecimal price = new BigDecimal(salesLine[1]);
				
				pricesList.add(price);
				
				
				if(!salesReportMap.containsKey(salesLine[0])) {
					salesReportMap.put(salesLine[0], 1);
				} else {
					salesReportMap.put(salesLine[0], salesReportMap.get(salesLine[0])+1);
					
				}
		
			}
		
		this.salesReportTotal = salesTotal(pricesList);
		
		salesLogScanner.close();
		
		return salesReportMap;

				
		}
	
	
	public BigDecimal salesTotal(List<BigDecimal> priceList) {
		
		BigDecimal listTotal = new BigDecimal("0.00");
		
		for (BigDecimal number : priceList) {
			listTotal = listTotal.add(number);
		}
		
		return listTotal;
		
	}
	
	public void outputSalesReport() throws IOException {
		
		String salesReportFileName = "SalesReports/Sales Report_" + LocalDate.now().format(salesReportDateFormatter) + "_" + LocalTime.now().format(salesReportTimeFormatter);
		
		
		try(VendingMachineLogger salesReport = new VendingMachineLogger(salesReportFileName)) {
			
			Map< String, Integer> salesReportMap = processSalesReport();
			
			for(Entry<String, Integer> entry: salesReportMap.entrySet()) {
				
				salesReport.write(entry.getKey() + "|" + entry.getValue());
				
			}
			
			salesReport.write(" ");
			salesReport.write("TOTAL SALES: $" + this.salesReportTotal);
		
		
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	

	public void acceptMoney(String dollarAmount) {
		String logEntry = LocalDate.now().format(dateFormatter) + " " + LocalTime.now().format(timeFormatter) + " FEED MONEY:";
		BigDecimal dollarAmountEntered = new BigDecimal(dollarAmount);
		logEntry = logEntry + " $" + currentMoneyBalance;
		currentMoneyBalance = currentMoneyBalance.add(dollarAmountEntered);
		logEntry = logEntry + " $" + currentMoneyBalance;
		System.out.println("Current Balance: " + currentMoneyBalance.toString());
		
		log.write(logEntry);
	}

	public void closeLogs() {
		
		
		try {
			salesLog.close();
			log.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void finishTransaction() {
		
		//Return all remaining balance in quarters, dimes, and nickles with least amount of coins possible
		String logEntry = LocalDate.now().format(dateFormatter) + " " + LocalTime.now().format(timeFormatter) + " GIVE CHANGE:";
		logEntry += " $" + currentMoneyBalance;
		
		int quarterCount = 0;
		int dimeCount = 0;
		int nickelCount = 0;
		BigDecimal convertDollarToPennies = new BigDecimal(100);
		
		int currentBalanceInPennies = currentMoneyBalance.multiply(convertDollarToPennies).intValue();
		
		while(currentBalanceInPennies >= 25) {
			
			currentBalanceInPennies -= 25;
			quarterCount++;
		}
		
		while(currentBalanceInPennies <25 && currentBalanceInPennies >= 10) {
			
			currentBalanceInPennies -= 10;
			dimeCount++;
			
		}
		
		while(currentBalanceInPennies <10 && currentBalanceInPennies >= 5) {
			
			currentBalanceInPennies -= 5;
			nickelCount++;
			
		}
		System.out.println("You have received " + quarterCount + " quarters, " + dimeCount + " dimes, and " + nickelCount + " nickels.  Congratulations!");
		
		

		// TODO return excess money function
		
		currentMoneyBalance = new BigDecimal("0.00");
		logEntry += " $" + currentMoneyBalance;
		log.write(logEntry);

	}

	public void displayItems() {
		TreeMap<String, StockedItem> sortedStockedItems = new TreeMap<>();
		sortedStockedItems.putAll(stockedItems);
		for (Map.Entry<String, StockedItem> entry : sortedStockedItems.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue().returnItemDisplayWithStock());
		}
	}

}
