package com.inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class InventoryItem {
	int amount;
	InboundOrder order;
	public InventoryItem(InboundOrder i, int a) {
		order = i;
		amount = a;
	}
	public String toString() {
		return "Item((" + order + "), amount: " + amount + ")";
	}
	public void setAmount(int a) {
		amount = a;
	}
}

public class Shelf {
	double width, height;
	List<InventoryItem> items;
	int capacity = 10;
	Depot depot;

	public Shelf(Depot d, double w, double h) {
		depot = d;
		width = w;
		height = h;
		items = new ArrayList<InventoryItem>();
	}
	
	public int getAvailableSpace() {
		Iterator<InventoryItem> it = items.iterator();
		int count = 0;
		while(it.hasNext()) {
			InventoryItem order = it.next();
			count += order.amount;
		}
		return capacity - count;
	}
	
	// @param(a): required amount. Not necessary same than order.amount
	public int storeItems(InboundOrder i, int a) {
		int available = getAvailableSpace();
		if (available > 0) {
			int leftSpace = available - a;
			InventoryItem item = new InventoryItem(i, leftSpace >= 0 ? a : available);
 			items.add(item);
 			depot.fireItemStoreComplete();
			return leftSpace >= 0 ? 0 : leftSpace * -1;
		} else {
			return a;
		}
	}
	
	// @param(a): required amount. Not necessary same than order.amount
	// Returns the amount left to complete @param(a)
	public int checkStockAvailability(StockTransfer st, int a) {
		Iterator<InventoryItem> it = items.iterator();
		int left = a;
		while(it.hasNext()) {
			InventoryItem item = it.next();
			Product p = item.order.request.getProduct();
			if (st.product.name.equals(p.name)) {
				left -= item.amount;
			}
			if (left <= 0) {
				break;
			}
		}
		return left <= 0 ? 0 : left;
	}

	public OutboundOrder transferItems(StockTransfer o, int a) {
		OutboundOrder order = new OutboundOrder(o);
		order.setAmount(0);
		Iterator<InventoryItem> it = items.iterator();
		int left = a;
		while(it.hasNext()) {
			InventoryItem item = it.next();
			Product p = item.order.request.getProduct();
			if (o.product.name.equals(p.name)) {
				int itemLeft = item.amount - left;
				if (itemLeft <= 0) {
					items.remove(item);
					left -= item.amount;
				} else {
					item.setAmount(itemLeft);
					left -= item.amount - itemLeft;
				}
			} else {
			}
			if (left <= 0) {
				break;
			}
		}
		depot.fireItemStoreComplete();
		if (left != a) {
			order.setAmount(left >= 0 ? a - left : a);
			return order;
		}
		return null;
	}
}
