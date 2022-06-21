package com.inventory;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

public class ProviderStatus extends ItemStatus implements ProviderEventListener {
	private static final long serialVersionUID = 1L;
	Provider provider;
	JLabel processingLabel;
	JLabel pausedLabel;
	public ProviderStatus(Provider p) {
		super("/icons/provider.png", "PROVEEDOR");
		provider = p;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		p.addInventoryEventListener(this);
		processingLabel = new JLabel("En Proceso:");
		pausedLabel = new JLabel("En Pausa:");
		
		add(processingLabel);
		add(pausedLabel);
	}
	@Override
	public void onStatusUpdate() {
		processingLabel.setText("En Proceso: " + provider.queue.size());
		pausedLabel.setText("En Pausa: " + provider.pausedQueue.size());
	}
}
