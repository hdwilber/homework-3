package com.inventory;

public class OutboundOrder extends TransferRequest {
	StockTransfer stockTransfer;
	int amount;
	public OutboundOrder(StockTransfer s) {
		this(s, s.priority);
	}

	public OutboundOrder(StockTransfer s, TransferRequestPriority p) {
		super(p);
		stockTransfer = s;
	}

	public void setAmount(int a) {
		amount = a;
	}
	
	public String getLabel() {
		return stockTransfer.product.name + "(" + amount + ", " + priority + ")";
	}
}
