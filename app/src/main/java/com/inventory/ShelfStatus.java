package com.inventory;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;

import com.inventory.taskrequest.TaskRequestStatus;

public class ShelfStatus {
	Shelf shelf;
	public ShelfStatus(Shelf s) {
		shelf = s;
	}

	public void drawProduct(InventoryItem item, Graphics g, int w, int h) {
		g.setColor(item.order.request.getProduct().color);
		if (item.order.getStatus() == TaskRequestStatus.COMPLETE) {
			g.fillRect(0, 0, w, h);
		} else if (item.order.getStatus() == TaskRequestStatus.IN_PROGRESS) {
			g.drawRect(0, 0, w, h);
		}
		g.setColor(PathStatusItem.palette[item.order.getPriority().getValue()]);
		g.fillOval(w - 35, h - 35, 25, 25);
		g.setColor(Color.black);
		g.drawString(item.order.request.getProduct().getName(), 25, 25);
		g.drawOval(w - 35, h - 35, 25, 25);
		g.drawString(item.order.getPriority().getValue() + "", w-25, h-15);
	}
	public void drawItems(Graphics g, int w, int h) {
		int padding = 10;
		int itemWidth = Math.round(w / shelf.capacity);
		int x = 0;
		int y = 0;
		
		int effItemWidth = itemWidth - (2 * padding);
		int effItemHeight = h - (2 * padding);
		
		synchronized(shelf.items) { 
			Iterator<InventoryItem> iter = shelf.items.iterator();
			while(iter.hasNext()) {
				InventoryItem item = iter.next();
				for(int i = 0; i < item.amount; i++) {
					Graphics productGraphics = g.create(x+ padding, y+padding, effItemWidth, effItemHeight);
					drawProduct(item, productGraphics, effItemWidth, effItemHeight);
					x += itemWidth;
				}
			}
		}
	}
	
	public void draw(Graphics g, int w, int h) {
//		g.setColor(Color.LIGHT_GRAY);
//		g.fillRect(0, 0, w, h);
		
		drawItems(g, w, h);
	}
}
