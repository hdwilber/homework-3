package com.inventory;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class DepotStatus extends JPanel implements DepotEventListener {
	private static final long serialVersionUID = 1L;
	Image image = new ImageIcon(getClass().getResource("/images/depot.png")).getImage();
	Depot depot;
	ShelfStatus[] shelves;
	public DepotStatus(Depot d) {
		super();
		depot = d;
		setupShelves();
		setPreferredSize(new Dimension(1000, 1000));
		d.addDepotEventListener(this);
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
		int y = (int)Math.round(height  * 0.40);
		int shelfHeight = Math.round((height - y) / shelves.length) - padding;
		int x = (int) Math.round(width * 0.1);
		int shelfWidth = width - (x * 2);
		for(int i = 0; i < shelves.length; i++) {
			Graphics subG = g.create(x, y, shelfWidth+1, shelfHeight+1);
			y += shelfHeight + padding;
			shelves[i].draw(subG, shelfWidth, shelfHeight);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		drawShelves(g);
	}

	@Override
	public void onDepotStoreComplete() {
	}

	@Override
	public void onDepotItemStoreComplete() {
		System.out.println("STORED");
		this.repaint();
	}

}
