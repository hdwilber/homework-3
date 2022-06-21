package com.inventory;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

public class StoreStatus extends ItemStatus {
	private static final long serialVersionUID = 1L;
	Store store;
	JLabel processingLabel;
	JLabel pausedLabel;
	public StoreStatus(Store s) {
		super("/icons/store.png", "TIENDA");
		store = s;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//		p.addInventoryEventListener(this);
		processingLabel = new JLabel("En Proceso:");
		pausedLabel = new JLabel("En Pausa:");
		
		add(processingLabel);
		add(pausedLabel);
	}
}