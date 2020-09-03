package com.techelevator.tenmo.models;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class TransferTest {

	@Test
	public void test_get_transfer_id() {
		Transfer testTransfer = new Transfer();
		testTransfer.setTransferId(1);
		assertEquals(1, testTransfer.getTransferId());
	}
	
	@Test
	public void test_get_transfer_type_id() {
		Transfer testTransfer = new Transfer();
		testTransfer.setTransferTypeId(2);
		assertEquals(2, testTransfer.getTransferTypeId());
	}
	
	@Test
	public void test_get_transfer_status_id() {
		Transfer testTransfer = new Transfer();
		testTransfer.setTransferStatusId(3);
		assertEquals(3, testTransfer.getTransferStatusId());
	}
	
	@Test
	public void test_get_account_from() {
		Transfer testTransfer = new Transfer();
		testTransfer.setAccountFrom(1);
		assertEquals(1, testTransfer.getAccountFrom());
	}
	
	@Test
	public void test_get_account_to() {
		Transfer testTransfer = new Transfer();
		testTransfer.setAccountTo(1);
		assertEquals(1, testTransfer.getAccountTo());
		
	}
	
	@Test
	public void test_get_amount() {
		Transfer testTransfer = new Transfer();
		testTransfer.setAmount(BigDecimal.valueOf(20.00));
		assertTrue(BigDecimal.valueOf(20.00).equals(testTransfer.getAmount()));
	}
	
	@Test
	public void test_get_transfer_type_string() {
		Transfer testTransfer = new Transfer();
		testTransfer.setTransferTypeId(1);
		assertEquals("Request", testTransfer.getTransferTypeString());
		testTransfer.setTransferTypeId(2);
		assertEquals("Send", testTransfer.getTransferTypeString());
	}

}
