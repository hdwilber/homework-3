package com.inventory;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

public class StorageStatus extends ItemStatus {
	ImageIcon icon;
	Inventory inventory;
	public StorageStatus(Inventory i) {
		super("/icons/storage.png", "ALMACEN");
		inventory = i;
	}
}
