package com.inventory.product;

public enum ProductType {
	DEHYDRATED("DESHIDRATADO"),
	FRESH("FRESCO"),
	MEAT("CARNES"),
	CLEANING("LIMPIEZA");
	
	private final String name;
	ProductType(String p) {
		name = p;
	}
	
	public String toString() {
		return name;
	}
}