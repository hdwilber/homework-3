package com.inventory.taskrequest;

public class InventoryInbound extends TaskRequest {
	public InventoryRequest request;
	public int amount;

	public InventoryInbound(InventoryRequest r) {
		super(r.priority);
		request = r;
		amount = r.amount;
	}
	public InventoryInbound(InventoryRequest r, int a) {
		super(r.priority);
		request = r;
		amount = a;
	}

	@Override
	public String getLabel() {
		return request.product.getName() + "(" + amount + ", " + priority + ")";
	}

}
