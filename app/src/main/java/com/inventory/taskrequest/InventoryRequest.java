package com.inventory.taskrequest;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.inventory.product.Product;

public class InventoryRequest extends TaskRequest {
	Product product;
	int amount;

	public InventoryRequest(Product p, int a) {
		this(p, a, TaskRequestPriority.NONE);
	}
	public InventoryRequest(Product pro, int a, TaskRequestPriority p) {
		super(p);
		product = pro;
		amount = a;
	}

	public Product getProduct() {
		return product;
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
