package com.inventory;

import java.awt.Color;

enum ProductType {
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

public class Product {
	private String name;
	ProductType type;
	Color color;
	public static Color[] palette = {
			new Color(Integer.valueOf("5f66fb", 16)),
			new Color(Integer.valueOf("00c7e0", 16)),
			new Color(Integer.valueOf("5ed599", 16)),
			new Color(Integer.valueOf("b5c85f", 16)),
			new Color(Integer.valueOf("d79650", 16)),
			new Color(Integer.valueOf("d16b6b", 16))
	};
	public static int colorCount = 0;

	public Product(String n, ProductType t)  {
		setName(n);
		type = t;
		if (colorCount >= palette.length) colorCount = 0;
		color = palette[colorCount];
		colorCount++;
	}
	
	public String toString() {
		return getName() + (type != null ? "(" + type.toString() +")": "");
	}

	public String getName() {
		return name;
	}

	public void setName(String n) {
		name = n;
	}
}
