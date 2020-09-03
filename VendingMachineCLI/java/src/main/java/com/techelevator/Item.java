package com.techelevator;

import java.math.BigDecimal;

public abstract class Item {
	
	private String name;
	private BigDecimal price;
	private String itemType;

	

	
	public Item() {

	}

	public abstract String getName();

	public abstract BigDecimal getPrice();

	public abstract String getItemType();
	

	
	public String getDispense(String itemType){
		
		
	
	if(itemType.equals("Chip")){ 
		return "Crunch, Crunch, Yum!";
				} 
	
	if(itemType.equals("Candy")){ 
		return "Munch Munch, Yum!";
				} 
	
	if(itemType.equals("Drink")){ 
		return  "Glug Glug, Yum!";
				} 
	
	if(itemType.equals("Gum")){ 
		return "Chew Chew, Yum!";
				} 
	
	return""; 
		
}
}
	
	
	
	
	
	
	