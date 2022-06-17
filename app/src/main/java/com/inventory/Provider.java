package com.inventory;

import java.util.ArrayList;
import java.util.List;

public class Provider {
	String name;
	List<Product> products;
	
	public Provider(String n) {
		name = n;
		products = new ArrayList<Product>();
	}
	
	public void addProduct(Product p) {
		products.add(p);
	}
	
	public String toString() {
		return name;
	}
}