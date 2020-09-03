package com.techelevator;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class StockedItemTest {

	@Test
	public void stocked_item_constructor_enter_chips_return_chips() {
		
		String name = "Chips";
		BigDecimal price = new BigDecimal(5);
		String itemType = "Snack";
		
		StockedItem testItem = new StockedItem(name, price, itemType);
		
		String expected = "Chips";
		
		assertEquals(expected,testItem.getName());
		
		
		
	}

}
