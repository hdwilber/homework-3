package com.inventory;

public class StockTransfer extends TransferRequest {
	Product product;
	int amount;

	public StockTransfer(Product pro, int a) {
		super();
		product = pro;
		amount = a;
	}

	public String getLabel() {
		return product.name + "(" + amount + ", " + priority + ")";
	}

}
