package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDAO {
	public List<Transfer> getAllTransfersByUser(long id);
	public void sendTransfer(long senderId, long receiverId, BigDecimal amount);
	public Transfer getTransferById(long transferId);
}
