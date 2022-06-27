package com.inventory.taskrequest;

public enum TaskRequestPriority {
	NONE(0, "NINGUNA"),
	LOW(1, "BAJA"),
	MIDDLE(2, "MEDIA"),
	HIGH(3, "ALTA"),
	VERY_HIGH(4, "MUY ALTA"),
	ALL_MIGHTY(5, "IMPARABLE");

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
}

