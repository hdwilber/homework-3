package com.inventory;

public class StoreStatus extends ItemStatus {
	private static final long serialVersionUID = 1L;
	Store store;
	public StoreStatus(Store s) {
		super("/icons/store.png", "TIENDA");
		store = s;
	}
}