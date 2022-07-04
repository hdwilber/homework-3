package com.inventory.product;

import java.util.Date;

import com.inventory.utils.DateUtils;

public class StockProduct {
	Product product;
	String batchNumber;
	Date expiryDate;
	
	public StockProduct(Product p, int d) {
		this(p, DateUtils.nextDays(d), null);
	}
	public StockProduct(Product p, Date e, String bn) {
		product = p;
		expiryDate = e;
		batchNumber = bn;
	}
	
	public Date getExpiryDate() {
		return expiryDate;
	}
}
