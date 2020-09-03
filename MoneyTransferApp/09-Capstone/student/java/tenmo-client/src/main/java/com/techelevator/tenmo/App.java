package com.techelevator.tenmo;

import java.math.BigDecimal;
import java.util.Scanner;

import org.springframework.web.client.HttpClientErrorException;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserService;
import com.techelevator.view.ConsoleService;

public class App {

	private static final String API_BASE_URL = "http://localhost:8080/";

	private static final String MENU_OPTION_EXIT = "Exit";
	private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN,
			MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	//private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	//private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS,
			MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };

	private AuthenticatedUser currentUser;
	private ConsoleService console;
	private AuthenticationService authenticationService;
	private TransferService transferService;
	private AccountService accountService;
	private UserService userService;
	private Scanner input;

	public static void main(String[] args) {
		App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
		app.run();
	}

	public App(ConsoleService console, AuthenticationService authenticationService) {
		this.console = console;
		this.authenticationService = authenticationService;
		transferService = new TransferService(API_BASE_URL);
		accountService = new AccountService(API_BASE_URL);
		userService = new UserService(API_BASE_URL);
		input = new Scanner(System.in);
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");

		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while (true) {
			String choice = (String) console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			//} else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				//viewPendingRequests(); TODO: implement feature
			} else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			//} else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
			//	requestBucks(); TODO: implement feature
			} else if (MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		try {
			BigDecimal balance = accountService.getAccountBalance(currentUser.getUser().getId(), currentUser.getToken());
			System.out.println("Your current account balance is: $" + balance);
		} catch (HttpClientErrorException e) {
			System.out.println("An error occurred");
		}
		console.next();

	}

	private void viewTransferHistory() {
		Transfer[] transferArray = transferService.viewAllTransfers(currentUser.getUser().getId(),
				currentUser.getToken());
		System.out.println("-----------------------------------");
		System.out.println("Transfers");
		System.out.printf("%-4s %-15s %-7s\n", "ID", "From/To", "Amount");
		System.out.println("-----------------------------------");
		for (int i = 0; i < transferArray.length; i++) {
			if (transferArray[i].getAccountFrom() == currentUser.getUser().getId()) {
				System.out.printf("%-4s %-15s %-7s\n", transferArray[i].getTransferId(),
				"To: " + userService.findUserById(transferArray[i].getAccountTo(),currentUser.getToken()).getUsername(), "$" + transferArray[i].getAmount());
				
			}else if(transferArray[i].getAccountTo() == currentUser.getUser().getId()) {
				System.out.printf("%-4s %-15s %-7s\n", transferArray[i].getTransferId(),

						"From: " + userService.findUserById(transferArray[i].getAccountFrom(),currentUser.getToken()).getUsername(), "$" + transferArray[i].getAmount());
			}
		}
		viewTransferDetails(transferArray);
		
	}
	
	private void viewTransferDetails(Transfer[] transferArray) {
		System.out.println();
		System.out.print("Please enter transfer ID to view details (0 to cancel): ");
		System.out.println();
		int choice = 0;
		boolean validChoice = false;
		while (validChoice == false) {
			try {
				choice = Integer.parseInt(input.nextLine());
				if (choice == 0) {
					validChoice = true;
					return;
				}else {
					for (Transfer transfer : transferArray) {
						if (transfer.getTransferId() == choice) {
							validChoice = true;
							
						} 
					}
					if (validChoice == false) {
						System.out.println("Invalid choice. Please pick from the list.");
					}
				}
			} catch (NumberFormatException e) {
				System.out.println();
				System.out.println("Invalid selection. Please enter a number. ");
				System.out.println();
				System.out.print("Please enter transfer Id to view details (0 to cancel): ");
			}
		}
		System.out.println("-----------------------------------");
		System.out.println("Transfer Details");
		System.out.println("-----------------------------------");
		for (Transfer transfer : transferArray) {
			if (transfer.getTransferId() == choice) {
				System.out.println("ID: " + transfer.getTransferId());
				System.out.println("From: " + userService.findUserById(transfer.getAccountFrom(), currentUser.getToken()).getUsername());
				System.out.println("To: " + userService.findUserById(transfer.getAccountTo(), currentUser.getToken()).getUsername());
				System.out.println("Type: " + transfer.getTransferTypeString());
				System.out.println("Status: " + transfer.getTransferStatusString());
				System.out.println("Amount: $" + transfer.getAmount());
			}
		}
		console.next();
	}

//	private void viewPendingRequests() {
//		// TODO Auto-generated method stub
//
//	}

	private void sendBucks() {
		User[] userArray = userService.getAllUsers(currentUser.getToken());
		System.out.println("-----------------------------------");
		System.out.println("Users");
		System.out.printf("%-4s %-15s\n", "ID", "Name");
		System.out.println("-----------------------------------");
		for (int i = 0; i < userArray.length; i++) {
			System.out.printf("%-4s %-15s\n",userArray[i].getId(), userArray[i].getUsername());
		}
		System.out.println();
		System.out.print("Enter ID of user you are sending to (0 to cancel): ");
		int choice = 0;
		boolean validChoice = false;
		while (validChoice == false) {
			try {
				choice = Integer.parseInt(input.nextLine());		
				if (choice == 0) {
					validChoice = true;
					return;
				}else if (choice == currentUser.getUser().getId()) {
					System.out.println("Cannot transfer to own account. Please try again.");
				}
				else {
					for (User user : userArray) {
						if (user.getId() == choice) {
							validChoice = true;
							
						} 
					}
					if (validChoice == false) {
						System.out.println("Invalid choice. Please pick from the list.");
					}
				}
			} catch (NumberFormatException e) {
				System.out.println();
				System.out.println("Invalid selection. Please enter a number. ");
				System.out.println();
				System.out.print("Enter ID of user you are sending to (0 to cancel): ");
			}
			
		}
		User receiver = null;
		for (User user : userArray) {
			if (user.getId() == userArray[choice - 1].getId()) {
				receiver = user;
			}
		}
		boolean validAmount = false;
		while (validAmount == false) {
			
			System.out.println();
			System.out.print("Enter transfer amount (0 to cancel): ");
			try{
				double transferDouble = Double.parseDouble(input.nextLine());
				if (transferDouble == 0) {
					System.out.println("Transaction cancelled.");
					console.next();
					return;
				}else if (transferDouble > 0) {
					BigDecimal transferAmount = BigDecimal.valueOf(transferDouble).setScale(2);
					Transfer theTransfer = transferService.sendTransfer(currentUser.getUser().getId(), receiver.getId(),
							transferAmount, currentUser.getToken());
					BigDecimal balance = accountService.getAccountBalance(currentUser.getUser().getId(), currentUser.getToken());
					System.out.println("Transfer successful. Current balance: $" + balance);
					validAmount = true;
					console.next();
				}else {
					System.out.println("Invalid amount. Cannot send zero or negative dollars.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid amount. Please try again.");
			}
			
		}

	}

//	private void requestBucks() {
//		// TODO Auto-generated method stub
//
//	}

	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while (!isAuthenticated()) {
			String choice = (String) console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
		while (!isRegistered) // will keep looping until user is registered
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				authenticationService.register(credentials);
				isRegistered = true;
				System.out.println("Registration successful. You can now login.");
			} catch (AuthenticationServiceException e) {
				System.out.println("REGISTRATION ERROR: " + e.getMessage());
				System.out.println("Please attempt to register again.");
			}
		}
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) // will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: " + e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}

	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
