package com.inventory.taskrequest;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.inventory.product.Product;
import com.inventory.product.StockProduct;

public class InventoryInbound extends TaskRequest {
	public InventoryRequest request;
	StockProduct stockProduct;
	public int amount;

	public InventoryInbound(InventoryRequest r, StockProduct sp) {
		this(r, sp, r.amount);
	}
	public InventoryInbound(InventoryRequest r, StockProduct sp, int a) {
		super(r.priority);
		request = r;
		amount = a;
		stockProduct = sp;
	}

	@Override
	public String getLabel() {
		return request.product.getName() + "(" + amount + ", " + priority + ")";
	}
	
	public Product getProduct() {
		return request.getProduct();
	}
	
	public StockProduct getStockProduct() {
		return stockProduct;
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

	public long getAmount() {
		return amount;
	}
}
