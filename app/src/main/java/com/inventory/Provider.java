package com.inventory;

import java.util.ArrayList;
import java.util.List;

public class Provider {
	Inventory inventory;
	String name;
	List<Product> products;
	List<TransferRequest> inboundOrders;
	
	public Provider(String n) {
		this(null, n);
	}
	public Provider(Inventory i, String n) {
		name = n;
		products = new ArrayList<Product>();
		inboundOrders = new ArrayList<TransferRequest>();
		inventory = i;
	}
	
	public void addProduct(Product p) {
		products.add(p);
	}
	
	public void setInventory(Inventory i) {
		inventory = i;
	}
	
	public void receiveRequest(Request r) {
		r.setStatus(TransferRequestStatus.RECEIVED);
		InboundOrder result = new InboundOrder(r);
		inboundOrders.add(result);
		inventory.addInboundOrder(null, result);
	}
	
	public String toString() {
		return name;
	}
}