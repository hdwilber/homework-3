package com.inventory;

enum ProductType {
	DEHYDRATED,
	FRESH,
	MEAT,
	CLEANING
}

public class Product {
	String name;
	ProductType type;

	public Product(String n, ProductType t)  {
		name = n;
		type = t;
	}
	
	public String toString() {
		return name + (type != null ? "(" + type.toString() +")": "");
	}
}
