package com.inventory;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import com.inventory.product.Product;
import com.inventory.product.StockProduct;
import com.inventory.taskrequest.InventoryInbound;
import com.inventory.taskrequest.InventoryRequest;
import com.inventory.taskrequest.TaskRequest;
import com.inventory.taskrequest.TaskRequestProcessor;

interface ProviderEventListener extends EventListener {
	public void onStatusUpdate();
}
public class Provider extends TaskRequestProcessor {
	Inventory inventory;
	String name;
	public List<Product> products;

	public Provider(String n, int c, int pc) {
		this(null, n, c, pc);
	}
	public Provider(Inventory i, String n, int c, int pc) {
		super(c, pc);
		name = n;
		products = new ArrayList<Product>();
		inventory = i;
	}
	
	public void addProduct(Product p) {
		products.add(p);
	}
	
	public void setInventory(Inventory i) {
		inventory = i;
	}
	
	int requestTotal = 0;
	
	@Override
	public void onTaskRequestComplete(TaskRequest r) {
		InventoryRequest request = (InventoryRequest)r;
		int expiryDays = (int) Math.round(Math.random() * 300);
		StockProduct sp = new StockProduct(request.getProduct(), expiryDays);
		InventoryInbound inbound = new InventoryInbound(request, sp);
		sendTask(inventory.requestsProcessor, inbound);
	}

	public long getProcessingTime(TaskRequest io) {
		InventoryRequest ir = (InventoryRequest)io;
//		long time = ((long)(Math.random() * 1000)) * ir.getAmount();
		long time = 250;
		return time;
	}

	public String toString() {
		return name;
	}
}