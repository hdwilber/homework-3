package com.inventory;

import java.util.Date;

enum RequestProrityType {
	NONE,
	LOW,
	MID,
	HIGH,
};

public class Request extends TransferRequest {
	Product product;
	int amount;
	Provider provider;

	public Request(Provider pro, Product p, int a) {
		product = p;
		amount = a;
		provider = pro;
	}
}
