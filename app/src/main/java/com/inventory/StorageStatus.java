package com.inventory;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

public class StorageStatus extends ItemStatus {
	ImageIcon icon;
	public StorageStatus() {
		super("/icons/storage.png", "ALMACEN");
	}
}
