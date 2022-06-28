package com.inventory.taskrequest;

import java.awt.Color;

public enum TaskRequestPriority {

	NONE(0, "NINGUNA"),
	LOW(1, "BAJA"),
	MIDDLE(2, "MEDIA"),
	HIGH(3, "ALTA"),
	VERY_HIGH(4, "MUY ALTA"),
	ALL_MIGHTY(5, "IMPARABLE");

	static Color[] priorityPalette = {
			new Color(Integer.valueOf("5f66fb", 16)),
			new Color(Integer.valueOf("00c7e0", 16)),
			new Color(Integer.valueOf("5ed599", 16)),
			new Color(Integer.valueOf("b5c85f", 16)),
			new Color(Integer.valueOf("d79650", 16)),
			new Color(Integer.valueOf("d16b6b", 16))
	};


	private final String name;
	private final int value;

	TaskRequestPriority(int v, String p) {
		value = v;
		name = p;
	}
	public String toString() {
		return name;
	}
	public int getValue() {
		return value;
	}
	
	public Color getColor() {
		return priorityPalette[getValue()];
	}

}

