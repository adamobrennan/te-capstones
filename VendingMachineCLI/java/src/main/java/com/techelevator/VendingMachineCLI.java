package com.techelevator;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.techelevator.view.Menu;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String MAIN_MENU_OPTION_SALES_REPORT = "Super Secret Sales Report";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT, MAIN_MENU_OPTION_SALES_REPORT};

	private static final String PURCHASE_MENU_OPTION_FEED_MONEY = "Feed money";
	private static final String PURCHASE_MENU_OPTION_SELECT_PRODUCT = "Select product";
	private static final String PURCHASE_MENU_OPTION_FINISH_TRANSACTION = "Finish transaction";
	private static final String[] PURCHASE_MENU_OPTIONS = { PURCHASE_MENU_OPTION_FEED_MONEY, PURCHASE_MENU_OPTION_SELECT_PRODUCT,
			PURCHASE_MENU_OPTION_FINISH_TRANSACTION };
	
	private static final String  STOP_FEEDING_MONEY = "Stop Feeding Money";
	private static final String  FEED_MONEY_OPTION_TEN_DOLLAR = "Ten Dollars";
    private static final String FEED_MONEY_OPTION_FIVE_DOLLAR = "Five Dollars";
	private static final String FEED_MONEY_OPTION_TWO_DOLLAR = "Two Dollars";
	private static final String FEED_MONEY_OPTION_ONE_DOLLAR = "One Dollar"; 
	private static final String[] FEED_MONEY_OPTIONS = { FEED_MONEY_OPTION_ONE_DOLLAR , FEED_MONEY_OPTION_TWO_DOLLAR , FEED_MONEY_OPTION_FIVE_DOLLAR
			, FEED_MONEY_OPTION_TEN_DOLLAR, STOP_FEEDING_MONEY
	};		

	
	
	private Menu menu;

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;

	}
	

	public void run() throws FileNotFoundException {
		
		VendingMachine vendingMachine = new VendingMachine();
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				vendingMachine.displayItems();
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				boolean inPurchaseMode = true;
				while (inPurchaseMode) {
					choice = (String) menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);
					if (choice.equals(PURCHASE_MENU_OPTION_FEED_MONEY)) {
						boolean inFeedMoneyMode = true; 	
						while (inFeedMoneyMode) {
							choice = (String) menu.getChoiceFromOptions(FEED_MONEY_OPTIONS);
							if ( choice.equals(FEED_MONEY_OPTION_ONE_DOLLAR)) {
								vendingMachine.acceptMoney("1.00");

							}
							else if ( choice.equals(FEED_MONEY_OPTION_TWO_DOLLAR)) {
								vendingMachine.acceptMoney("2.00"); 
							}
							
							else if ( choice.equals(FEED_MONEY_OPTION_FIVE_DOLLAR )) {
								vendingMachine.acceptMoney("5.00");
							}
							else if (choice.equals(FEED_MONEY_OPTION_TEN_DOLLAR)){
								vendingMachine.acceptMoney("10.00");
							}
							
							else if (choice.equals(STOP_FEEDING_MONEY)){
								inFeedMoneyMode = false;
							}
						
						}
					
					}else if (choice.equals(PURCHASE_MENU_OPTION_SELECT_PRODUCT)) {
						vendingMachine.displayItems();
						System.out.println( "Please enter out the product code of the item you would like to purchase.");
						vendingMachine.getItemCodeFromUserAndDispense();
					}
					
					else if(choice.equals(PURCHASE_MENU_OPTION_FINISH_TRANSACTION)) {
						// TODO finish transaction
						vendingMachine.finishTransaction();
						inPurchaseMode = false;
					}
				}
			} else if(choice.equals(MAIN_MENU_OPTION_EXIT)){
				System.out.println("Thank you for visiting the Team 2 Vendomatic Slightly Sassy Vending Machine, brought to you by the Umbrella Corporation");
				vendingMachine.closeLogs();
				System.exit(1);
			} else if(choice.equals(MAIN_MENU_OPTION_SALES_REPORT)) {
				try {
					vendingMachine.outputSalesReport();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
	}
}
