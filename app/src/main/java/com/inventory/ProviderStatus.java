package com.inventory;

import com.inventory.taskrequest.*;

public class ProviderStatus extends ItemStatus implements TaskRequestEventListener {
	private static final long serialVersionUID = 1L;
	public ProviderStatus(Provider p) {
		super(p, "/icons/provider.png", "PROVEEDOR");
	}
}
