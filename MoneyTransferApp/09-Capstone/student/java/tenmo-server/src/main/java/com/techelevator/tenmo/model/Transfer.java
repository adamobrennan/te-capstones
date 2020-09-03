package com.techelevator.tenmo.model; 

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

public class Transfer {

	private long transferId;
	@Min(value = 1, message = "The field 'transferType' cannot be blank")
	private int transferTypeId;
	@Min(value = 1, message = "The field 'transferStatusId' cannot be blank")
	private int transferStatusId;
	@Min(value = 1, message = "The field 'accountFrom' cannot be blank")
	private long accountFrom;
	@Min(value = 1, message = "The field 'accountTo' cannot be blank")
	private long accountTo;
	@DecimalMin(value = "0", message = "The field 'amount' cannot be less than zero")
	private BigDecimal amount;

	public Transfer() {

	}

	public Transfer(long transferId, int transferTypeId, int transferStatusId, long accountFrom, long accountTo,
			BigDecimal amount) {
		this.transferId = transferId;
		this.transferTypeId = transferTypeId;
		this.transferStatusId = transferStatusId;
		this.accountFrom = accountFrom;
		this.accountTo = accountTo;
		this.amount = amount;
	}

	public Transfer(int transferTypeId, int transferStatusId, long accountFrom, long accountTo, BigDecimal amount) {
		this.transferTypeId = transferTypeId;
		this.transferStatusId = transferStatusId;
		this.accountFrom = accountFrom;
		this.accountTo = accountTo;
		this.amount = amount;
	}

	public long getTransferId() {
		return transferId;
	}

	public void setTransferId(long transferId) {
		this.transferId = transferId;
	}

	public int getTransferTypeId() {
		return transferTypeId;
	}

	public void setTransferTypeId(int transferTypeId) {
		this.transferTypeId = transferTypeId;
	}

	public int getTransferStatusId() {
		return transferStatusId;
	}

	public void setTransferStatusId(int transferStatusId) {
		this.transferStatusId = transferStatusId;
	}

	public long getAccountFrom() {
		return accountFrom;
	}

	public void setAccountFrom(long accountFrom) {
		this.accountFrom = accountFrom;
	}

	public long getAccountTo() {
		return accountTo;
	}

	public void setAccountTo(long accountTo) {
		this.accountTo = accountTo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
