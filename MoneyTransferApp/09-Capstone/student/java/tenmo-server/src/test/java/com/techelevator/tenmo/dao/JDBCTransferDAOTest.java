package com.techelevator.tenmo.dao;


import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.tenmo.model.Transfer;

public class JDBCTransferDAOTest {
	
	private static SingleConnectionDataSource dataSource;
	private TransferDAO dao;
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
		dao = new JDBCTransferDAO(jdbcTemplate);
		
	}
	@Test
	public void testGetAllTransfersByUser() {
		List<Transfer> expectedTransfers = new ArrayList<>();
		String sqlGetAllTransfersByUser = "SELECT * FROM transfers t " 
				+ "INNER JOIN accounts a "
				+ "ON t.account_from = a.account_id "
				+ "INNER JOIN users u " 
				+ "ON a.user_id = u.user_id "
				+ "WHERE t.account_from = ? " 
				+ "OR t.account_to = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllTransfersByUser, 1, 1);
		while (results.next()) {
			Transfer theTransfer = mapRowToTransfer(results);
			expectedTransfers.add(theTransfer);
		}
		List<Transfer> actualTransfers = dao.getAllTransfersByUser(1);
		assertEquals(actualTransfers, expectedTransfers);
	}
	
	@Test
	public void testGetTransferById() {
		Transfer expectedTransfer = new Transfer();
		String sqlGetTransferById = "SELECT * FROM transfers WHERE transfer_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetTransferById, 3);
		results.next();
		expectedTransfer = mapRowToTransfer(results);
		Transfer actualTransfer = dao.getTransferById(3);
		
		assertTransfersEqual(expectedTransfer, actualTransfer);
	}
	
	
	private void assertTransfersEqual(Transfer expectedTransfer, Transfer actualTransfer) {
		assertEquals(expectedTransfer.getTransferId(), actualTransfer.getTransferId());
		assertEquals(expectedTransfer.getTransferTypeId(), actualTransfer.getTransferTypeId());
		assertEquals(expectedTransfer.getTransferStatusId(), actualTransfer.getTransferStatusId());
		assertEquals(expectedTransfer.getAccountFrom(), actualTransfer.getAccountFrom());
		assertEquals(expectedTransfer.getAccountTo(), actualTransfer.getAccountTo());
		assertEquals(expectedTransfer.getAmount(), actualTransfer.getAmount());
		
	}

	private Transfer mapRowToTransfer(SqlRowSet results) {
		Transfer theTransfer = new Transfer();
		theTransfer.setTransferId(results.getLong("transfer_id"));
		theTransfer.setTransferTypeId(results.getInt("transfer_type_id"));
		theTransfer.setTransferStatusId(results.getInt("transfer_status_id"));
		theTransfer.setAccountFrom(results.getLong("account_from"));
		theTransfer.setAccountTo(results.getLong("account_to"));
		theTransfer.setAmount(results.getBigDecimal("amount"));
		return theTransfer;
	}

}
