package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Transfer;

@Component
public class JDBCTransferDAO implements TransferDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCTransferDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Transfer> getAllTransfersByUser(long id) {
		List<Transfer> transferList = new ArrayList<>();
		String sqlGetAllTransfersByUser = "SELECT * FROM transfers t " 
										+ "INNER JOIN accounts a "
										+ "ON t.account_from = a.account_id "
										+ "INNER JOIN users u " 
										+ "ON a.user_id = u.user_id "
										+ "WHERE t.account_from = ? " 
										+ "OR t.account_to = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllTransfersByUser, id, id);
		while (results.next()) {
			Transfer theTransfer = mapRowToTransfer(results);
			transferList.add(theTransfer);

		}
		return transferList;
	}

	@Override
	public void sendTransfer(long senderId, long receiverId, BigDecimal amount) {
		String sqlSendTransfer = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) "
								+ "VALUES (?,?,(SELECT account_id FROM accounts WHERE user_id = ?),(SELECT account_id FROM accounts WHERE user_id = ?),?) ";
		jdbcTemplate.update(sqlSendTransfer, 2, 2, senderId, receiverId, amount);

	}

	@Override
	public Transfer getTransferById(long transferId) {
		String sqlGetTransferById = "SELECT * FROM transfers WHERE transfer_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetTransferById, transferId);
		results.next();
		Transfer theTransfer = mapRowToTransfer(results);
		return theTransfer;
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
