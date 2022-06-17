package com.inventory;

public class StockTransfer extends TransferRequest {
	Product product;
	int amount;

	public StockTransfer(Product p, int a, TransferRequestPriority priority) {
		super(priority);
		product = p;
		amount = a;
	}

}
