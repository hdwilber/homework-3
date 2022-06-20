package com.inventory;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import javax.swing.event.EventListenerList;

interface DepotEventListener extends EventListener {
	public void onDepotStoreComplete();
	public void onDepotItemStoreComplete();
}

public class Depot {
	Shelf[] shelves;
	double width, height;
    EventListenerList listenerList = new EventListenerList();

	public Depot(double w, double h, int count) {
		width = w;
		height = h;
		setupShelves(count);
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
	
	public boolean store(InboundOrder o) {
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

	public boolean canMeetStockTransfer(StockTransfer o) {
		int left = o.amount;
		for(int i = 0; i < shelves.length; i++) {
			Shelf shelf = shelves[i];
			left = shelf.checkStockAvailability(o, left);
			if (left <= 0) {
				break;
			}
		}
		return left <= 0;
	}
	
	public List<OutboundOrder> transfer(StockTransfer st) {
		List<OutboundOrder> result = new ArrayList<OutboundOrder>();
		int left = st.amount;
		for(int i = 0; i < shelves.length; i++) {
			Shelf shelf = shelves[i];
			OutboundOrder subOrder = shelf.transferItems(st, left);
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
	
	int total = 0;
	public boolean receiveInboundOrder(InboundOrder o) {
		o.setStatus(TransferRequestStatus.PROCESSING);
		boolean succeed = store(o);
		o.setStatus(TransferRequestStatus.PROCESSED);
		fireItemStoreComplete();
		if (succeed) {
			total += o.amount;
			System.out.println("TOTAL " + total);
		} else {
			total -= o.amount;
			System.out.println("TOTAL WITH REJECETED " + total);
		}
		return succeed;
	}
	
	public List<OutboundOrder> receiveStockTransfer(StockTransfer st) {
		boolean succeed = canMeetStockTransfer(st);
		if(succeed) {
			return transfer(st);
		}
		return null;
	}
}
