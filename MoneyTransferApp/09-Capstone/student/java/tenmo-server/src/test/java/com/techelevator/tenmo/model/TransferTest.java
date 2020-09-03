package com.techelevator.tenmo.model;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class TransferTest { 

	@Test
	public void testGetTransferId() {
		Transfer testTransfer =  new Transfer(1, 2, 2, 1, 2, BigDecimal.valueOf(1000));
		assertEquals(1, testTransfer.getTransferId());
	}
	@Test
	public void testGetTransferTypeId() {
		Transfer testTransfer =  new Transfer(1, 2, 2, 1, 2, BigDecimal.valueOf(1000));
		assertEquals(2, testTransfer.getTransferTypeId());
}
	@Test
	public void testGetTransferStatusId() {
		Transfer testTransfer =  new Transfer(1, 2, 2, 1, 2, BigDecimal.valueOf(1000));
		assertEquals(2, testTransfer.getTransferStatusId());
	
	}
	@Test
	public void testGetAccountFrom() {
		Transfer testTransfer =  new Transfer(1, 2, 2, 1, 2, BigDecimal.valueOf(1000));
		assertEquals(1, testTransfer.getAccountFrom());
	
	}
	@Test
	public void testGetAccountTo() {
		Transfer testTransfer =  new Transfer(1, 2, 2, 1, 2, BigDecimal.valueOf(1000));
		assertEquals(2, testTransfer.getAccountTo());
	}
	@Test
	public void testGetAmount() {
		Transfer testTransfer =  new Transfer(2, 2, 1, 2, BigDecimal.valueOf(1000));
		assertEquals(BigDecimal.valueOf(1000), testTransfer.getAmount()); 
}
	@Test
	public void testSetTransferId() {
		Transfer testTransfer =  new Transfer(1, 2, 2, 1, 2, BigDecimal.valueOf(1000));
		testTransfer.setTransferId(2);
		assertEquals(2, testTransfer.getTransferId());
	}
	@Test
	public void testSetTransferTypeId() {
		Transfer testTransfer =  new Transfer(1, 2, 2, 1, 2, BigDecimal.valueOf(1000));
		testTransfer.setTransferTypeId(1);
		assertEquals(1, testTransfer.getTransferTypeId());
}
	@Test
	public void testSetTransferStatusId() {
		Transfer testTransfer =  new Transfer(1, 2, 2, 1, 2, BigDecimal.valueOf(1000));
		testTransfer.setTransferStatusId(1);
		assertEquals(1, testTransfer.getTransferStatusId());
	
	}

@Test 
public void testSetAccountFrom() {
	Transfer testTransfer =  new Transfer(1, 2, 2, 1, 2, BigDecimal.valueOf(1000));
	testTransfer.setAccountFrom(2);
	assertEquals(2, testTransfer.getAccountFrom());
 
}
@Test
public void testSetAccountTo() {
	Transfer testTransfer =  new Transfer(1, 2, 2, 1, 2, BigDecimal.valueOf(1000));
	testTransfer.setAccountTo(1);
	assertEquals(1, testTransfer.getAccountTo());
}
@Test
public void testSetAmount() {
	Transfer testTransfer =  new Transfer(2, 2, 1, 2, BigDecimal.valueOf(1000));
	testTransfer.setAmount(BigDecimal.valueOf(500.00));
	assertEquals(BigDecimal.valueOf(500.00), testTransfer.getAmount()); 
}
}