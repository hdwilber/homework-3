package com.inventory;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class StorageStatus extends ItemStatus implements StoreStatusListener {
	private static final long serialVersionUID = 1L;
	ImageIcon icon;
	Inventory inventory;
	JLabel processingLabel;
	JLabel pausedLabel;
	public StorageStatus(Inventory i) {
		super("/icons/storage.png", "ALMACEN");
		inventory = i;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		processingLabel = new JLabel("");
		pausedLabel = new JLabel("");
		inventory.addStorageStatusListener(this);
		
		add(processingLabel);
		add(pausedLabel);
		
	}

	@Override
	public void onStoreStatusUpdate() {
		processingLabel.setText("En Proceso: " + inventory.queue.size());
		pausedLabel.setText("En Pausa: " + inventory.pausedQueue.size());
	}
}
