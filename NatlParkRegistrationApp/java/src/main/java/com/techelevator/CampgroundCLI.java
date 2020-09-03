package com.techelevator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.dao.DataIntegrityViolationException;

import com.techelevator.campground.jdbc.JDBCCampgroundDAO;
import com.techelevator.campground.jdbc.JDBCParkDAO;
import com.techelevator.campground.jdbc.JDBCReservationDAO;
import com.techelevator.campground.jdbc.JDBCSiteDAO;
import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.Site;
import com.techelevator.view.Menu;

public class CampgroundCLI {
	
	private Menu menu;
	private JDBCParkDAO parkDao;
	private JDBCCampgroundDAO campgroundDao;
	private JDBCReservationDAO reservationDao;
	private JDBCSiteDAO siteDao;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	private Scanner userInput;
	List<Park> parkList;

	private static final String PARK_MENU_VIEW_CAMPGROUNDS = "View Campgrounds";
	private static final String PARK_MENU_SEARCH_FOR_RESERVATION = "Search for Reservation";
	private static final String PARK_MENU_RETURN_TO_PREVIOUS_SCREEN = "Return to previous screen";

	private static String[] PARK_MENU_OPTIONS = new String[] { PARK_MENU_VIEW_CAMPGROUNDS,
			PARK_MENU_SEARCH_FOR_RESERVATION, PARK_MENU_RETURN_TO_PREVIOUS_SCREEN };
	
	private static final String CAMPGROUND_MENU_SEARCH_FOR_RESERVATION = "Search for Reservation";
	private static final String CAMPGROUND_MENU_RETURN_TO_PREVIOUS_SCREEN = "Return to previous screen";

	private static String[] CAMPGROUND_MENU_OPTIONS = new String[] { CAMPGROUND_MENU_SEARCH_FOR_RESERVATION,
			CAMPGROUND_MENU_RETURN_TO_PREVIOUS_SCREEN };

	public static void main(String[] args) throws SQLException {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/CampgroundReservation");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		CampgroundCLI application = new CampgroundCLI(dataSource);

		application.run();
	}

	public CampgroundCLI(DataSource dataSource) {
		parkDao = new JDBCParkDAO(dataSource);
		campgroundDao = new JDBCCampgroundDAO(dataSource);
		reservationDao = new JDBCReservationDAO(dataSource);
		siteDao = new JDBCSiteDAO(dataSource);
		this.menu = new Menu(System.in, System.out);
		this.userInput = new Scanner(System.in);
		parkList = parkDao.getAllParks();
	}

	public void run() throws SQLException {
		while (true) {

			printHeading("Select a Park for further details: ");
			
			String[] parkArray = new String[parkList.size() + 1];// length one more so we can add quit
			for (int i = 0; i < parkArray.length - 1; i++) { // loop through the list of park
				parkArray[i] = parkList.get(i).getParkName(); // add the name to our array of strings
			}
			parkArray[parkArray.length - 1] = "Quit"; // now add quit to last position
			String choice = (String) menu.getChoiceFromOptions(parkArray);
			if (choice.equals("Quit")) { // handle the quit
				System.out.println("Thank you for using the Park Reservation application!");
				System.exit(1); // ends the program
			}
			for (Park park : parkList) {
				if (choice.equals(park.getParkName())) {
					printParkInfo(park);
				}
			}
			printHeading("Select a command: ");
			String parkMenuChoice = (String) menu.getChoiceFromOptions(PARK_MENU_OPTIONS);
			if (parkMenuChoice.equals(PARK_MENU_VIEW_CAMPGROUNDS)) {
				handleViewCampgrounds(choice);
				String campgroundMenuChoice = (String) menu.getChoiceFromOptions(CAMPGROUND_MENU_OPTIONS);
				if (campgroundMenuChoice.equals(CAMPGROUND_MENU_SEARCH_FOR_RESERVATION)) {
					handleReservationSearch(choice);
				} else if (campgroundMenuChoice.equals(CAMPGROUND_MENU_RETURN_TO_PREVIOUS_SCREEN)) {
					return;
				}
			} else if (parkMenuChoice.equals(PARK_MENU_SEARCH_FOR_RESERVATION)) {
				handleReservationSearch(choice);
			} else if (parkMenuChoice.equals(PARK_MENU_RETURN_TO_PREVIOUS_SCREEN)) {
				return;
			}
		}
	}

	private void handleReservationSearch(String choice) throws SQLException {
		
		String[] parkArray = new String[parkList.size()];
		for (int i = 0; i < parkArray.length; i++) {
			parkArray[i] = parkList.get(i).getParkName();
		}
		System.out.print("Which campground? (Enter 0 to cancel)? ");
		
		int parkId = 0;
		for (Park park : parkList) {
			if (choice.equals(park.getParkName())) {
				parkId = park.getParkId();
			}
		}
		List<Campground> campgroundList = campgroundDao.searchCampgroundByPark(parkId);
		String[] campgroundArray = new String[campgroundList.size()];
		for (int i = 0; i < campgroundArray.length; i++) {
			campgroundArray[i] = campgroundList.get(i).getName();
		}
		String campgroundChoice = (String) menu.getChoiceFromOptions(campgroundArray);
		if (campgroundChoice.equals("0")) {

		} else {
			int campgroundId = 0;
			for (Campground campground : campgroundList) {
				if (campgroundChoice.equals(campground.getName())) {
					campgroundId = campground.getCampgroundId();
				}
			}
			boolean validDateEntry = false;
			while (validDateEntry == false) {
				try {
					System.out.print("What is the arrival date? __/__/____");
					LocalDate fromDate = LocalDate.parse(userInput.nextLine(), formatter);
					
					
					System.out.print("What is the departure date? __/__/____");
					LocalDate toDate = LocalDate.parse(userInput.nextLine(), formatter);
					List<Site> siteList = siteDao.searchSiteByCampground(campgroundId);
					handleMakeCampgroundReservation(siteList, fromDate, toDate);
					validDateEntry = true;
					
				} catch (DateTimeParseException e) {
					System.out.println("Invalid date entry.");
				}				
			}
		}
	}

	private void handleMakeCampgroundReservation(List<Site> siteList, LocalDate fromDate, LocalDate toDate) throws SQLException {
		System.out.println("Results matching your search criteria");
		System.out.printf("%-8s %-10s %-12s %-13s %-7s %-7s\n", "Site No.", "Max Occup.", "Accessible?",
				"Max RV Length", "Utility", "Cost");
		int siteId = 0;
		boolean validSiteId = false;
		while (validSiteId == false) {
			for (Site site : siteList) {
				if (reservationDao.isReserved(site, fromDate, toDate) == false) {
					int campgroundId = site.getCampgroundId();
					System.out.printf("%-8s %-10s %-12s %-13s %-7s %-7s\n", site.getSiteNumber(), 
																			site.getMaxOccupancy(),
																			site.isAccessible() == true ? "Yes" : "No", 
																			site.getMaxRvLength() == 0 ? "N/A" : site.getMaxRvLength(), 
																			site.isUtilities() == true ? "Yes" : "No", 
																			"$" + siteDao.calculateTotalFee(fromDate, toDate, campgroundId).setScale(2));
					
				}
			}
			if(siteList.isEmpty()) {
				System.out.println("No available sites for selected dates");
				return;
			}
			try {
				System.out.print("Select a site by number: ");
				siteId = Integer.parseInt(userInput.nextLine());
				
				System.out.print("Create reservation? (Y/N) ");
				String createReservation = userInput.nextLine().toUpperCase();
				
				if (createReservation.equals("Y")) {
					System.out.println("What name should the reservation be made under?");
					String reservationName = userInput.nextLine();
					try {
						int reservationId = reservationDao.createReservation(siteId, reservationName, fromDate, toDate);
						System.out.println("The reservation has been made and the confirmation id is " + reservationId);
						validSiteId = true;
						
					} 
					catch (DataIntegrityViolationException e) {
						System.out.println("Invalid site.");
					}
					
				} else if (createReservation.equals("N")) {
					return;
				} else {
					System.out.println("Invalid selection");
					return;
				}
			}
			catch (NumberFormatException e) {
				System.out.println("Invalid entry");
			}	
		}
	}

	private void handleViewCampgrounds(String choice) {
		List<Park> parkList = parkDao.getAllParks();
		for (Park park : parkList) {
			if (choice.equals(park.getParkName())) {
				listCampgrounds(park);
			}
		}

	}

	private void listCampgrounds(Park park) {
		System.out.println(park.getParkName() + " National Park Campgrounds");
		System.out.printf("%-4s %-18s %-9s %-11s %-15s\n", " ", "Name", "Open", "Close", "Daily Fee");
		List<Campground> campgroundList = campgroundDao.searchCampgroundByPark(park.getParkId());
		for (int i = 0; i < campgroundList.size(); i++) {
			System.out.printf("%-4s %-18s %-9s %-11s %-15s", ("#" + (i + 1) + " "), campgroundList.get(i).getName(),
					Month.of(campgroundList.get(i).getOpenFrom()), Month.of(campgroundList.get(i).getOpenTo()),
					("$" + campgroundList.get(i).getDailyFee().setScale(2)));
			System.out.println();

		}

	}

	private void printParkInfo(Park park) {
		System.out.println(park.getParkName());
		System.out.println("Location:        " + park.getLocation());
		System.out.println("Established:     " + park.getEstablishedDate().format(formatter));
		System.out.println("Area:            " + park.getArea() + " sq km");
		System.out.println("Annual Visitors: " + String.format("%,d", park.getVisitors()));
		System.out.println();
		System.out.println(park.getDescription());

	}

	private void printHeading(String headingText) {
		System.out.println("\n" + headingText);
		for (int i = 0; i < headingText.length(); i++) {
			System.out.print("-");
		}
		System.out.println();

	}
}
