package com.inventory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.inventory.product.Product;

public class DepotStatus extends JPanel implements DepotEventListener, InventoryEventListener {
	private static final long serialVersionUID = 1L;
	Image image = new ImageIcon(getClass().getResource("/images/depot.png")).getImage();
	Depot depot;
	ShelfStatus[] shelves;
	Product selectedProduct;
	ItemStatus status;
	
	public DepotStatus(Depot d) {
		super();
		depot = d;
		setBackground(Color.WHITE);
		setupShelves();
		setMinimumSize(new Dimension(500, 400));
		setPreferredSize(new Dimension(1000, 800));
		d.addDepotEventListener(this);
		d.inventory.addInventoryEventListener(this);

		setLayout(new BorderLayout());
		ItemStatus status = new ItemStatus(d, null, d.getName()) {
			private static final long serialVersionUID = 1L;
		};
		status.setOpaque(false);
		depot.addTaskRequestEventListener(status);
		add(status, BorderLayout.NORTH);
	}

	public void setupShelves() {
		int count = depot.shelves.length;
		shelves = new ShelfStatus[count];
		for(int i = 0; i < count; i++) {
			ShelfStatus shelfStatus = new ShelfStatus(depot.shelves[i]);
			shelves[i] = shelfStatus;
		}
	}
	
	public void drawShelves(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		int padding = 20;
		int y = (int)Math.round(height  * 0.20);
		int shelfHeight = Math.round((height - y) / shelves.length) - padding;
		int x = (int) Math.round(width * 0.05);
		int shelfWidth = width - (x * 2);
		for(int i = 0; i < shelves.length; i++) {
			Graphics subG = g.create(x, y, shelfWidth+1, shelfHeight+1);
			y += shelfHeight + padding;
			shelves[i].draw(subG, shelfWidth, shelfHeight, selectedProduct);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 16, 16, getWidth() -32, getHeight() -32, this);
		drawShelves(g);
	}

	@Override
	public void onDepotStoreComplete() {
	}

	@Override
	public void onDepotItemStoreComplete() {
//		System.out.println("STORED");
		this.repaint();
	}

	@Override
	public void onTransferRequestUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProductSelected(Product p) {
		selectedProduct = p;
		this.repaint();
	}

}
