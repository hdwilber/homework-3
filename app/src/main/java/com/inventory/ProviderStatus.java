package com.inventory;

public class ProviderStatus extends ItemStatus {
	private static final long serialVersionUID = 1L;
	Provider provider;
	public ProviderStatus(Provider p) {
		super("/icons/provider.png", "PROVEEDOR");
		provider = p;
	}
}
