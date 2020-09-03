package com.techelevator.campground.jdbc;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.temporal.ChronoUnit;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.Site;
import com.techelevator.campground.model.SiteDAO;

public class JDBCSiteDAO implements SiteDAO {

private JdbcTemplate jdbcTemplate;
	
	public JDBCSiteDAO (DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public BigDecimal calculateTotalFee(LocalDate fromDate, LocalDate toDate, int campgroundId) {
		BigDecimal reservations = new BigDecimal(0);
		String sqlGetDailyFee = "SELECT daily_fee FROM campground "
							  + "WHERE campground_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetDailyFee, campgroundId);
		results.next();
		
		BigDecimal dailyFee = results.getBigDecimal("daily_fee");
		
		int diff = (int) ChronoUnit.DAYS.between(fromDate, toDate);
		
		BigDecimal totalFee = dailyFee.multiply(BigDecimal.valueOf(diff));
	    
	    return totalFee; 
	}

	
	
	
	@Override
	public List<Site> searchSiteByCampground(int campgroundId) {
		List<Site> siteList = new ArrayList<>();
		String sqlsearchSiteByCampground = "SELECT * FROM site WHERE campground_id = ? ORDER BY campground_id ASC, site_id ASC LIMIT 5";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlsearchSiteByCampground, campgroundId);
		while (results.next()) {
			Site theSite = mapRowToSite(results);
			siteList.add(theSite);
		}
		return siteList;
	}

	private Site mapRowToSite(SqlRowSet results) {
		Site theSite = new Site();
		theSite.setSiteId(results.getInt("site_id"));
		theSite.setCampgroundId(results.getInt("campground_Id"));
		theSite.setSiteNumber(results.getInt("site_number"));
		theSite.setMaxOccupancy(results.getInt("max_occupancy"));
		theSite.setAccessible(results.getBoolean("accessible"));
		theSite.setMaxRvLength(results.getInt("max_rv_length"));
		theSite.setUtilities(results.getBoolean("utilities"));
		
		return theSite;
	}
}
