package com.inventory;

public class OutboundOrder extends TransferRequest {
	StockTransfer stockTransfer;
	public OutboundOrder(StockTransfer s) {
		this(s, TransferRequestPriority.NONE);
	}
	public OutboundOrder(StockTransfer s, TransferRequestPriority p) {
		super(p);
		stockTransfer = s;
	}
}
