package com.inventory;

import java.awt.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ProviderStatus extends ItemStatus {
	List<Provider> providers;

	public ProviderStatus(List<Provider> p) {
		super("/icons/provider.png", "PROVEEDOR");
		providers = p;
	}
}
