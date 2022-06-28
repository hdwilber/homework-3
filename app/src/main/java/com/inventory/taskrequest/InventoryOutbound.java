package com.inventory.taskrequest;

import com.inventory.product.Product;

public class InventoryOutbound extends TaskRequest {
	public InventoryStockTransfer request;
	public int amount;

	public InventoryOutbound(InventoryStockTransfer r) {
		super(r.priority);
		request = r;
		amount = r.amount;
	}
	public InventoryOutbound(InventoryStockTransfer r, int a) {
		super(r.priority);
		request = r;
		amount = a;
	}

	@Override
	public String getLabel() {
		return request.product.getName() + "(" + amount + ", " + priority + ")";
	}
	
	public void setAmount(int a) {
		amount = a;
	}
	
	public int getAmount() {
		return amount;
	}
	
	@Override
	public Product getProduct() {
		return request.getProduct();
	}

}
