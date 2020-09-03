package com.techelevator.campground.jdbc;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.SQLException;
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

import com.techelevator.campground.model.Campground;

public class JDBCCampgroundDAOTest {

	private JDBCCampgroundDAO dao;
	
	private static SingleConnectionDataSource dataSource;
	
	@BeforeClass
	public static void setUpDataSource() throws Exception {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/CampgroundReservation");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}

	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}

	@Before
	public void setUp() {
		dao = new JDBCCampgroundDAO(dataSource);
	}
	
	@After 
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
		
	}
	@Test
	public void test_search_campground_by_park() {
		List<Campground> expectedCampgroundList = new ArrayList<>();
		String sqlSearchCampgroundByPark = "SELECT c.* FROM campground c "
											+ "INNER JOIN park p "
											+ "ON c.park_id = p.park_id "
											+ "WHERE p.park_id = (SELECT park_id FROM park WHERE name = 'Acadia') "
											+ "ORDER BY p.name ASC, c.name ASC";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchCampgroundByPark);
		while (results.next()) {
			Campground theCampground = getCampground(results.getInt("campground_id"), 
										results.getInt("park_id"),
										results.getString("name"),
										results.getInt("open_from_mm"),
										results.getInt("open_to_mm"),
										results.getBigDecimal("daily_fee"));
			expectedCampgroundList.add(theCampground);
		}
		List<Campground> actualCampgroundList = dao.searchCampgroundByPark(expectedCampgroundList.get(0).getParkId());
		for (int i = 0; i < expectedCampgroundList.size(); i++) {
			assertCampgroundsEqual(expectedCampgroundList.get(i), actualCampgroundList.get(i));
		}
		assertEquals(expectedCampgroundList.size(), actualCampgroundList.size());
	}
	
	public void assertCampgroundsEqual(Campground expected, Campground actual) {
		assertEquals(expected.getCampgroundId(), actual.getCampgroundId());
		assertEquals(expected.getParkId(), actual.getParkId());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getOpenFrom(), actual.getOpenFrom());
		assertEquals(expected.getOpenTo(), actual.getOpenTo());
		assertEquals(expected.getDailyFee(), actual.getDailyFee());
		
	}
	
	public Campground getCampground(int campgroundId, int parkId, String name, 
									int openFrom, int openTo, BigDecimal dailyFee) {
		
		Campground theCampground = new Campground();
		theCampground.setCampgroundId(campgroundId);
		theCampground.setParkId(parkId);
		theCampground.setName(name);
		theCampground.setOpenFrom(openFrom);
		theCampground.setOpenTo(openTo);
		theCampground.setDailyFee(dailyFee);
		
		return theCampground;
	}

}
