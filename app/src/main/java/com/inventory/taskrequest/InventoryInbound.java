package com.inventory.taskrequest;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.inventory.product.Product;

public class InventoryInbound extends TaskRequest {
	public InventoryRequest request;
	public int amount;

	public InventoryInbound(InventoryRequest r) {
		super(r.priority);
		request = r;
		amount = r.amount;
	}
	public InventoryInbound(InventoryRequest r, int a) {
		super(r.priority);
		request = r;
		amount = a;
	}

	@Override
	public String getLabel() {
		return request.product.getName() + "(" + amount + ", " + priority + ")";
	}
	
	@Override
	public Product getProduct() {
		return request.getProduct();
	}

	@Override
	public void setContentInfo(JPanel panel, ImageIcon icon, Font font) {
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		JLabel label = new JLabel(icon);
		label.setOpaque(false);
		label.setFont(font);
		label.setText(getProduct().getName() + "(" + amount +")");
		label.setHorizontalTextPosition(JLabel.CENTER);
		label.setVerticalTextPosition(JLabel.TOP);
		label.setForeground(Color.white);
		panel.setBackground(getPriority().getColor());
		panel.add(label);
	}
}
