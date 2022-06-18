package com.inventory;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class DepotStatus extends JPanel {
	Image image = new ImageIcon(getClass().getResource("/images/depot.png")).getImage();
	public DepotStatus() {
		super();
		setPreferredSize(new Dimension(1000, 1000));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
	}

}
