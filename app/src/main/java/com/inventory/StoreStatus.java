package com.inventory;

public class StoreStatus extends ItemStatus {
	Store store;
	public StoreStatus(Store s) {
		super("/icons/store.png", "TIENDA");
		store = s;
	}
}