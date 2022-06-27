package com.inventory;

public class StoreStatus extends ItemStatus {
	private static final long serialVersionUID = 1L;
	public StoreStatus(Store s) {
		super(s, "/icons/store.png", "TIENDA");
	}
}