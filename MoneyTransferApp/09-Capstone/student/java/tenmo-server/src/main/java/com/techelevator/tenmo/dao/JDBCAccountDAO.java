package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Account;
@Component
public class JDBCAccountDAO implements AccountDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCAccountDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public BigDecimal getAccountBalance(long accountId) {
		String sqlGetAccountBalance = "SELECT * FROM accounts WHERE account_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAccountBalance, accountId);
		results.next();
		Account theAccount = mapRowToAccount(results);
		BigDecimal accountBalance = theAccount.getBalance();
		return accountBalance;
	}
	
	

	@Override
	public void updateSenderBalance(long senderId, BigDecimal transferAmount) {
		String sqlUpdateSenderBalance = "UPDATE accounts "
										+ "SET balance = ? "
										+ "WHERE account_id = ?";
		jdbcTemplate.update(sqlUpdateSenderBalance, (getAccountBalance(senderId).subtract(transferAmount)), senderId);
		
	}
	
	@Override
	public void updateReceiverBalance(long receiverId, BigDecimal transferAmount) {
		String sqlUpdateReceiverBalance = "UPDATE accounts "
										+ "SET balance = ? "
										+ "WHERE account_id = ?";
		jdbcTemplate.update(sqlUpdateReceiverBalance, (getAccountBalance(receiverId).add(transferAmount)), receiverId);
		
	}
	private Account mapRowToAccount(SqlRowSet results) {
		Account theAccount = new Account();
		theAccount.setAccountId(results.getLong("account_id"));
		theAccount.setUserId(results.getLong("user_id"));
		theAccount.setBalance(results.getBigDecimal("balance"));

		return theAccount;
	}


}
