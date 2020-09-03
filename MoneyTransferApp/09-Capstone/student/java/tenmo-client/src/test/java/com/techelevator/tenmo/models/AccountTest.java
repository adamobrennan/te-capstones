package com.techelevator.tenmo.models;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class AccountTest {

	@Test
	public void test_get_account_id() {
		Account testAccount = new Account();
		testAccount.setAccountId(1);
		assertEquals(1, testAccount.getAccountId());
	}
	
	@Test
	public void test_get_balance() {
		Account testAccount = new Account();
		testAccount.setBalance(BigDecimal.valueOf(20.00));
		assertTrue(BigDecimal.valueOf(20.00).equals(testAccount.getBalance()));
	}
	
	@Test
	public void test_get_user_id() {
		Account testAccount = new Account();
		testAccount.setUserId(1);;
		assertEquals(1, testAccount.getUserId());
	}

}
