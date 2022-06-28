package com.inventory.taskrequest;

import com.inventory.product.Product;

public class InventoryStockTransfer extends TaskRequest {
	Product product;
	int amount;

	public InventoryStockTransfer(Product p, int a) {
		this(p, a, TaskRequestPriority.NONE);
	}
	public InventoryStockTransfer(Product pro, int a, TaskRequestPriority p) {
		super(p);
		product = pro;
		amount = a;
	}

	@Override
	public String getLabel() {
		return product.getName() + "[" +priority + "](" + amount + ")";
	}
	
	public Product getProduct() {
		return product;
	}
	
	public int getAmount() {
		return amount;
	}
}
