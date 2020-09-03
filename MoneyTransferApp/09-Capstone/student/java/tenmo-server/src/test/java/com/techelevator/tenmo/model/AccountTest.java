package com.techelevator.tenmo.model;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class AccountTest {

	@Test
	public void testGetAccountId() {
		Account testAccount = new Account(1, 1, BigDecimal.valueOf(1000.00));
		assertEquals(1, testAccount.getAccountId());
		
	}
	@Test
	public void testGetUserId() {
		Account testAccount = new Account(1, 1, BigDecimal.valueOf(1000.00));
		assertEquals(1, testAccount.getUserId());	
}
	@Test
	public void testGetBalance() {
		Account testAccount = new Account(1, 1, BigDecimal.valueOf(1000.00));
		assertEquals(BigDecimal.valueOf(1000.00),testAccount.getBalance());
	}
	 @Test
	 public void testSetAccountId() { 
		 Account testAccount = new Account(1, 1, BigDecimal.valueOf(1000.00));
		 testAccount.setAccountId(2);
		 assertEquals(2, testAccount.getAccountId());
	 } 
	 @Test
		public void testSetUserId() { 
			Account testAccount = new Account(1, 1, BigDecimal.valueOf(1000.00));
			testAccount.setUserId(2);
			assertEquals(2, testAccount.getUserId());	
	} 
	 @Test
		public void testSetBalance() {
			Account testAccount = new Account(1, BigDecimal.valueOf(1000.00));
			testAccount.setBalance(BigDecimal.valueOf(500.00));
			assertEquals(BigDecimal.valueOf(500.00),testAccount.getBalance());
			
		}
}