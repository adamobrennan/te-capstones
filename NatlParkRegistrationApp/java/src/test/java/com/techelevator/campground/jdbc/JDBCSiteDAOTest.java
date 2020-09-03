package com.techelevator.campground.jdbc;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Site;

public class JDBCSiteDAOTest {
	
private JDBCSiteDAO dao;
	
	private static SingleConnectionDataSource dataSource;
	private static DateTimeFormatter formatter;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/CampgroundReservation");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
		formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		dataSource.destroy();
	}

	@Before
	public void setUp() {
		dao = new JDBCSiteDAO(dataSource);
	}
	
	@After 
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
		
	}
	@Test
	public void test_calculate_Total_Fee() {
		LocalDate fromDate = LocalDate.parse("06/05/2020", formatter);
		LocalDate toDate = LocalDate.parse("06/07/2020", formatter);
		int campgroundId = 1;
		
		String sqlGetDailyFee = "SELECT daily_fee FROM campground "
				  				+ "WHERE campground_id = ?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetDailyFee, campgroundId);
		results.next();
		BigDecimal expectedDailyFee = results.getBigDecimal("daily_fee");
		int diff = (int)ChronoUnit.DAYS.between(fromDate, toDate);
		BigDecimal expectedTotalFee = expectedDailyFee.multiply(BigDecimal.valueOf(diff));
		BigDecimal actualTotalFee = dao.calculateTotalFee(fromDate, toDate, campgroundId);
		assertEquals(expectedTotalFee, actualTotalFee);
	}
	
	@Test
	public void test_search_site_by_campground() {
		List<Site> expectedSiteList = new ArrayList<>();
		int campgroundId = 1;
		String sqlsearchSiteByCampground = "SELECT * FROM site WHERE campground_id = ? ORDER BY campground_id ASC, site_id ASC LIMIT 5";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlsearchSiteByCampground, campgroundId);
		while (results.next()) {
			Site theSite = mapRowToSite(results);
			expectedSiteList.add(theSite);
		}
		List<Site> actualSiteList = dao.searchSiteByCampground(campgroundId);
		for(int i = 0; i < expectedSiteList.size(); i++) {
			assertSiteEqual(expectedSiteList.get(i), actualSiteList.get(i));
		}
}
	private void assertSiteEqual(Site site, Site site2) {
		assertEquals(site.getSiteId(), site2.getSiteId());
		assertEquals(site.getCampgroundId(), site2.getCampgroundId());
		assertEquals(site.getSiteNumber(), site2.getSiteNumber());
		assertEquals(site.getMaxOccupancy(), site2.getMaxOccupancy());
		assertEquals(site.isAccessible(), site2.isAccessible());
		assertEquals(site.getMaxRvLength(), site2.getMaxRvLength());
		assertEquals(site.isUtilities(), site2.isUtilities());
		
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