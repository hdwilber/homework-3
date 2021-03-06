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
	@Override
	public long getProcessingTime(TaskRequest tr) {
		if (tr instanceof InventoryStockTransfer) {
			return ((InventoryStockTransfer)tr).getAmount() * 500;
		} else if (tr instanceof InventoryInbound) {
			return ((InventoryInbound)tr).getAmount() * 500;
		}
		return 1000;
	}

	Shelf[] shelves;
	double width, height;
	Inventory inventory;
    EventListenerList listenerList = new EventListenerList();
    volatile int currentTotal;
    String name;
    static int counter = 1;

	public Depot(Inventory i, double w, double h, int count) {
		super(2, 2);
		width = w;
		height = h;
		setupShelves(count);
		inventory = i;

		name = "Depósito: " +counter;
		counter++;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public boolean receiveTask(TaskRequest ts) {
		if (ts instanceof InventoryInbound) {
			InventoryInbound i = (InventoryInbound)ts;
			currentTotal += i.amount;
			return super.receiveTask(ts);
		} else if (ts instanceof InventoryStockTransfer) {
			return super.receiveTask(ts);
		}
		return false;
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
	public int countCurrentStockByProduct(Product p) {
		int count = 0;
		for(int i = 0; i < shelves.length; i++) {
			Shelf shelf = shelves[i];
			count += shelf.countCurrentStockByProduct(p);
		}
		return count;
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
		if (r instanceof InventoryInbound) {
			InventoryInbound i = (InventoryInbound)r;
			currentTotal -= i.amount;
			store(i);
			i.setStatus(TaskRequestStatus.COMPLETE);
		} else if (r instanceof InventoryStockTransfer) {

			List<InventoryOutbound> list = transfer((InventoryStockTransfer)r);
			list.forEach(outbound -> {
				sendTask(inventory.outboundsProcessor, outbound);
			});

//			InventoryInbound i = (InventoryInbound)r;
//			currentTotal -= i.amount;
//			store(i);
//			i.setStatus(TaskRequestStatus.COMPLETE);
		}
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
