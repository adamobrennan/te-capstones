package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Transfer {

	public Transfer() {
	}
	
	
		private long transferId;
		private long transferTypeId;
		private long transferStatusId;
		private long accountFrom;
		private long accountTo;
		private BigDecimal amount;
		
		public long getTransferId() {
			return transferId;
		}
		public void setTransferId(long transferId) {
			this.transferId = transferId;
		}
		public long getTransferTypeId() {
			return transferTypeId;
		}
		public void setTransferTypeId(long transferTypeId) {
			this.transferTypeId = transferTypeId;
		}
		public long getTransferStatusId() {
			return transferStatusId;
		}
		public void setTransferStatusId(long transferStatusId) {
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
		
		public String getTransferTypeString() {
			String transferType = null;
			if (transferTypeId == 1) {
				transferType = "Request";
			}else if (transferTypeId == 2) {
				transferType = "Send";
			}
			return transferType;
		}
		
		public String getTransferStatusString() {
			String transferStatus = null;
			if (transferStatusId == 1) {
				transferStatus = "Pending";
			}else if (transferStatusId == 2) {
				transferStatus = "Approved";
			}else if (transferStatusId == 3) {
				transferStatus = "Rejected";
			}
			return transferStatus;
		}
		
		
		
	

}
