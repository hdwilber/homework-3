package com.inventory;

import java.util.Date;

public class Request extends TransferRequest {
	Product product;
	int amount;
	Provider provider;

	public Request(Provider pro, Product p, int a) {
		this(pro, p, a, TransferRequestPriority.NONE);
	}
	public Request(Provider pro, Product p, int a, TransferRequestPriority priority) {
		super(priority);
		product = p;
		amount = a;
		provider = pro;
	}
}
