package com.inventory;

import javax.swing.ImageIcon;

public class StorageStatus extends ItemStatus {
	private static final long serialVersionUID = 1L;
	ImageIcon icon;
	Inventory inventory;
	public StorageStatus(Inventory i) {
		super("/icons/storage.png", "ALMACEN");
		inventory = i;
	}
}
