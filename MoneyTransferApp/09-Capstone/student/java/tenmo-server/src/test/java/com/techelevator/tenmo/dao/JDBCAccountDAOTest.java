package com.techelevator.tenmo.dao;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.tenmo.model.Account;


public class JDBCAccountDAOTest {

	private static SingleConnectionDataSource dataSource;
	private AccountDAO dao;
	private static JdbcTemplate jdbcTemplate;
	
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		/* The following line disables autocommit for connections
		 * returned by this DataSource. This allows us to rollback
		 * any changes after each test */
		dataSource.setAutoCommit(false);
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/* After all tests have finished running, this method will close the DataSource */
	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}	
	
	@Before
	public void setup() {
		dao = new JDBCAccountDAO(jdbcTemplate);
		String sqlCreateTestAccount = "INSERT INTO accounts (account_id, user_id, balance) "
									+ "VALUES (?,?,?)";
		jdbcTemplate.update(sqlCreateTestAccount, 999, 999, 1000.00);
		
		
	}

	/* After each test, we rollback any changes that were made to the database so that
	 * everything is clean for the next test */
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void testGetAccountBalance() {
		Account expectedAccount = new Account();
		String sqlGetAccountBalance =  "SELECT * FROM accounts WHERE account_id = ?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAccountBalance, 1);
		results.next();
		expectedAccount = mapRowToAccount(results);
		BigDecimal actualBalance = dao.getAccountBalance(1);
		assertTrue(expectedAccount.getBalance().equals(actualBalance));
	}
	
	
	
	private Account mapRowToAccount(SqlRowSet results) {
		Account theAccount = new Account();
		theAccount.setAccountId(results.getLong("account_id"));
		theAccount.setUserId(results.getLong("user_id"));
		theAccount.setBalance(results.getBigDecimal("balance"));

		return theAccount;
	}
}
