package com.techelevator.tenmo.models;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UserTest {

	@Test
	public void test_get_id() {
		User testUser = new User();
		testUser.setId(1);
		assertTrue(1 == testUser.getId());
	}
	
	@Test
	public void test_get_username() {
		User testUser = new User();
		testUser.setUsername("user");
		assertTrue(testUser.getUsername().equals("user"));
	}

}
