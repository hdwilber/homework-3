package com.inventory;

public class InboundOrder extends TransferRequest {
	Request request;
	int amount;
	public InboundOrder(Request r) {
		this(r, r.priority, r.amount);
	}
	public InboundOrder(Request r, TransferRequestPriority p) {
		this(r, p, r.amount);
	}
	public InboundOrder(Request r, TransferRequestPriority p, int a) {
		super(p);
		request = r;
		amount = a;
	}

	public String getLabel() {
		return request.product.name + "(" + amount + ", " + priority + ")";
	}
}
