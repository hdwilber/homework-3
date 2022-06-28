package com.inventory;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import javax.swing.event.EventListenerList;

import com.inventory.product.Product;
import com.inventory.taskrequest.InventoryInbound;
import com.inventory.taskrequest.InventoryOutbound;
import com.inventory.taskrequest.InventoryStockTransfer;
import com.inventory.taskrequest.TaskRequest;
import com.inventory.taskrequest.TaskRequestProcessor;
import com.inventory.taskrequest.TaskRequestStatus;

interface DepotEventListener extends EventListener {
	public void onDepotStoreComplete();
	public void onDepotItemStoreComplete();
}

public class Depot extends TaskRequestProcessor {
	Shelf[] shelves;
	double width, height;
    EventListenerList listenerList = new EventListenerList();
    volatile int currentTotal;

	public Depot(double w, double h, int count) {
		super(1, 1);
		width = w;
		height = h;
		setupShelves(count);
	}

	@Override
	public boolean receiveTask(TaskRequest ts) {
		InventoryInbound i = (InventoryInbound)ts;
		currentTotal += i.amount;
		return super.receiveTask(ts);
	}

    public void addDepotEventListener(DepotEventListener l) {
        listenerList.add(DepotEventListener.class, l);
    }

    public void removeDepotEventListener(DepotEventListener l) {
        listenerList.remove(DepotEventListener.class, l);
    }

    public void fireDepotStoreComplete() {
    	Object[] listeners = listenerList.getListenerList();
    	for (int i = listeners.length-2; i >= 0; i-=2) {
    		if (listeners[i] == DepotEventListener.class) {
    			((DepotEventListener)listeners[i+1]).onDepotStoreComplete();
    		}
    	}
    }

    public void fireItemStoreComplete() {
    	Object[] listeners = listenerList.getListenerList();
    	for (int i = listeners.length-2; i >= 0; i-=2) {
    		if (listeners[i] == DepotEventListener.class) {
    			((DepotEventListener)listeners[i+1]).onDepotItemStoreComplete();
    		}
    	}
    }

	public void setupShelves(int count) {
		shelves = new Shelf[count];
		for(int i = 0; i < count; i++) {
			Shelf shelf = new Shelf(this, width, height/count);
			shelves[i] = shelf;
		}
	}
	
	public int getAvailableSpace() {
		int count = 0;
		for(int i = 0; i < shelves.length; i++) {
			count += shelves[i].getAvailableSpace();
		}
		return count;
	}
	
	public boolean store(InventoryInbound o) {
		if (o.amount <= getAvailableSpace()) {
			int left = o.amount;
			for(int i = 0; i < shelves.length; i++) {
				Shelf shelf = shelves[i];
				left = shelf.storeItems(o, left);
				if (left <= 0) {
					break;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public List<InventoryItem> getCurrentStockByProduct(Product p) {
		List<InventoryItem> list = new ArrayList<InventoryItem>();
		for(int i = 0; i < shelves.length; i++) {
			Shelf shelf = shelves[i];
			List<InventoryItem> currentStock = shelf.getCurrentStockByProduct(p);
			if (currentStock.size() > 0) {
				list.addAll(currentStock);
			}
		}
		return list;
	}

	public boolean canMeetStockTransfer(InventoryStockTransfer o) {
		int left = o.getAmount();
		for(int i = 0; i < shelves.length; i++) {
			Shelf shelf = shelves[i];
			left = shelf.checkStockAvailability(o, left);
			if (left <= 0) {
				break;
			}
		}
		return left <= 0;
	}
	
	public boolean canReceiveInbound(InventoryInbound in) {
		int count = 0;
		for(int i = 0; i < shelves.length; i++) {
			count += shelves[i].getAvailableSpace();
		}
		return (in.amount - (count - currentTotal)) <= 0;
	}
	
	public List<InventoryOutbound> transfer(InventoryStockTransfer st) {
		List<InventoryOutbound> result = new ArrayList<InventoryOutbound>();
		int left = st.getAmount();
		for(int i = 0; i < shelves.length; i++) {
			Shelf shelf = shelves[i];
			InventoryOutbound subOrder = shelf.transferItems(st, left);
			if (subOrder != null) {
				result.add(subOrder);
				left -= subOrder.amount;
			}
			if (left <= 0) {
				break;
			}
		}
		fireItemStoreComplete();
		return result;
	}

	@Override
	public void onTaskRequestComplete(TaskRequest r) {
		InventoryInbound i = (InventoryInbound)r;
		currentTotal -= i.amount;
		store(i);
		i.setStatus(TaskRequestStatus.COMPLETE);
	}
	
//	public boolean receiveInventoryInbound(InventoryInbound i) {
//		i.setStatus(TaskRequestStatus.IN_PROGRESS);
//		return true;
//	}

	public List<InventoryOutbound> receiveStockTransfer(InventoryStockTransfer st) {
		boolean succeed = canMeetStockTransfer(st);
		if(succeed) {
			return transfer(st);
		}
		return null;
	}
}
