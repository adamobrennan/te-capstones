package com.techelevator.campground.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Site;

public class JDBCReservationDAOTest {
	
	private static JDBCReservationDAO dao;
	private static SingleConnectionDataSource dataSource;
	private static DateTimeFormatter formatter;
	
	@BeforeClass
	public static void setUpDataSource() throws Exception {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/CampgroundReservation");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
		formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	}

	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}

	@Before
	public void setUp() {
		
		dao = new JDBCReservationDAO(dataSource);
	}
	
	@After 
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
		
	}
	@Test
	public void test_create_reservation() {
		int siteId = 1;
		String name = "Smith Family Reservation";
		LocalDate fromDate = LocalDate.parse("06/05/2020", formatter);
		LocalDate toDate = LocalDate.parse("06/07/2020", formatter);
		
		int expectedReservationId = dao.createReservation(siteId, name, fromDate, toDate);
		String sqlGetActualReservationId = "SELECT reservation_id FROM reservation "
										+ "WHERE name = 'Smith Family Reservation' AND from_date = '2020-06-05'::date";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetActualReservationId);
		results.next();
		int actualReservationId = results.getInt("reservation_id");
		
		assertEquals(expectedReservationId, actualReservationId);
	}
	
	@Test
	public void test_is_reserved() {
		Site site = new Site();
		site.setSiteId(1);
		LocalDate fromDate = LocalDate.parse("06/16/2020", formatter);
		LocalDate toDate = LocalDate.parse("06/20/2020", formatter);
		
		boolean expectedResult = true;
		boolean actualResult = dao.isReserved(site, fromDate, toDate);
		
		assertTrue(expectedResult == actualResult);
	}

}
