package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface AccountDAO {
	public BigDecimal getAccountBalance (long accountId);
	public void updateSenderBalance(long senderId, BigDecimal amount);
	public void updateReceiverBalance(long receiverId, BigDecimal amount);
}
