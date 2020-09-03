package com.techelevator;

import java.math.BigDecimal;

public class StockedItem extends Item{
	//private Item itemHeld;
	private int stockLevel;
	private String name;
	private BigDecimal price;
	private String itemType;
	private String dispenseMessage;
	
	
					
	
	public StockedItem(String name, BigDecimal price, String itemType) {
		
		this.name = name;
		this.price = price;
		this.itemType = itemType;
		this.dispenseMessage = super.getDispense(itemType);
		
		this.stockLevel = 5;
		
		
	}
	

	public void decreaseStockByOne() {
		
		if(inStock() == true) {
			this.stockLevel--;
		}
		
	}
	
	public boolean inStock() {
		
		if(this.stockLevel > 0) {		
			return true;
		}	
		return false;
	}




	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}



	@Override
	public BigDecimal getPrice() {
		// TODO Auto-generated method stub
		return this.price;
	}





	@Override
	public String getItemType() {
		// TODO Auto-generated method stub
		return this.itemType;
	}




	public int getStockLevel() {
		return stockLevel;
	}


	public String getDispenseMessage() {
		return dispenseMessage;
	}
	
	public String returnItemDisplayWithStock() {
		if (stockLevel == 0) {
			return name + ", " + price + ", SOLD OUT";
		}else {
			return name + ", $" + price; // + ", " + stockLevel + " left in stock";  if you would like to display stock level
		}
	}
	
	
	
	
}
