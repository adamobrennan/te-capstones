package com.techelevator.campground.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface SiteDAO {

	
	public List<Site> searchSiteByCampground(int campgroundId);
	

	

	
	public BigDecimal calculateTotalFee(LocalDate fromDate, LocalDate toDate, int campgroundId); 
	
}
