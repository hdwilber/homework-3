package com.inventory;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ItemStatus extends JPanel {
	private static final long serialVersionUID = 1L;
	ImageIcon icon;
	public ItemStatus(String i, String l) {
		icon = new ImageIcon(
				new ImageIcon(getClass().getResource(i))
				.getImage()
				.getScaledInstance(75, 75, java.awt.Image.SCALE_SMOOTH)
		);
		JLabel label = new JLabel(l);
		setBorder(new EmptyBorder(4, 4, 4, 4));
		label.setIcon(icon);
		label.setHorizontalTextPosition(JLabel.CENTER);
		label.setVerticalTextPosition(JLabel.BOTTOM);
		label.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(label);
	}
}
