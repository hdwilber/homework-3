package com.inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.inventory.taskrequest.InventoryInbound;
import com.inventory.taskrequest.InventoryOutbound;
import com.inventory.taskrequest.InventoryStockTransfer;

class InventoryItem {
	int amount;
	InventoryInbound order;
	public InventoryItem(InventoryInbound i, int a) {
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
	public int storeItems(InventoryInbound i, int a) {
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
	public int checkStockAvailability(InventoryStockTransfer st, int a) {
		Iterator<InventoryItem> it = items.iterator();
		int left = a;
		while(it.hasNext()) {
			InventoryItem item = it.next();
			Product p = item.order.request.getProduct();
			if (st.getProduct().getName().equals(p.getName())) {
				left -= item.amount;
			}
			if (left <= 0) {
				break;
			}
		}
		return left <= 0 ? 0 : left;
	}

	public InventoryOutbound transferItems(InventoryStockTransfer o, int a) {
		InventoryOutbound order = new InventoryOutbound(o);
		order.setAmount(0);
		Iterator<InventoryItem> it = items.iterator();
		int left = a;
		while(it.hasNext()) {
			InventoryItem item = it.next();
			Product p = item.order.request.getProduct();
			if (o.getProduct().getName().equals(p.getName())) {
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

	public List<InventoryItem> getCurrentStockByProduct(Product p) {
		List<InventoryItem> list = new ArrayList<InventoryItem>();
		Iterator<InventoryItem> it = items.iterator();
		while(it.hasNext()) { 
			InventoryItem item = it.next();
			Product product = item.order.request.getProduct();
			if (product.getName().equals(p.getName())) {
				list.add(item);
			}
		}
		return list;
	}
}
