package com.techelevator.campground.jdbc;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Park;

public class JDBCParkDAOTest {

	
	private JDBCParkDAO dao;
	
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
		dao = new JDBCParkDAO(dataSource);
	}
	
	@After 
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
		
	}
	@Test
	public void test_get_all_parks() {
		String sqlGetAllDepartments = "SELECT COUNT (*) FROM park";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllDepartments);
		results.next();
		int expected = results.getInt(1);
		List<Park> actualParks = dao.getAllParks();
		assertTrue(expected == actualParks.size());
		
				
	}

}
