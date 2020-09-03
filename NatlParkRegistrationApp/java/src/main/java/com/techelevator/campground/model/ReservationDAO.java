package com.techelevator.campground.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ReservationDAO {

	public int createReservation(int siteId, String name, LocalDate fromDate, LocalDate toDate);
	
	public boolean isReserved(Site site, LocalDate fromDate, LocalDate toDate);
	
	
	
	
	
	
	
}
