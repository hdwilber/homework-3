package com.inventory;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import javax.swing.event.EventListenerList;

import com.inventory.product.Product;
import com.inventory.product.ProductType;
import com.inventory.taskrequest.*;

interface InventoryEventListener extends EventListener {
	public void onTransferRequestUpdate();
	public void onProductSelected(Product p);
}
interface StoreStatusListener extends EventListener {
	public void onStoreStatusUpdate();
}

public class Inventory {
	List<Depot> depots;
	List<Provider> providers;
	Store store;
	Product selectedProduct;
	List<AutomaticRequest> automaticRequests;
	int totalRequests = 0;
	int totalInbounds = 0;
	int totalTransfers = 0;
	int totalOutbounds = 0;

	EventListenerList listenerList = new EventListenerList();
	EventListenerList storeListenerList = new EventListenerList();

	public TaskRequestProcessor requestsProcessor = new TaskRequestProcessor(1, 1) {
		public long getProcessingTime(TaskRequest io) {
			InventoryRequest ir = (InventoryRequest)io;
			long time = ((long)(Math.random() * 1000)) * ir.getAmount();
//			long time = 200;
			return time;
		}
		@Override
		public void onTaskRequestComplete(TaskRequest r) {
			if (r instanceof InventoryRequest) { 
				totalRequests++;
				InventoryRequest request = (InventoryRequest)r;
				Provider provider = providers.get(0);
				provider.receiveTask(request);

			} else if (r instanceof InventoryInbound) {
				totalInbounds++;
				receiveInventoryInbound((InventoryInbound)r);
			}
		}
	};

	public TaskRequestProcessor outboundsProcessor = new TaskRequestProcessor(1, 1) {
		public long getProcessingTime(TaskRequest io) {
			InventoryStockTransfer ir = (InventoryStockTransfer)io;
			long time = ((long)(Math.random() * 1000)) * ir.getAmount();
//			long time = 200;
			return time;
		}
		@Override
		public void onTaskRequestComplete(TaskRequest r) {
			if (r instanceof InventoryStockTransfer) {
				totalTransfers++;
				receiveStockTransfer((InventoryStockTransfer)r);
			} else if (r instanceof InventoryOutbound){
				totalOutbounds++;
				sendTask(store, r);
			}
		}
	};

	public Inventory() {
		super();
		depots = new ArrayList<Depot>();
		depots.add(new Depot(this, 10, 10, 5));
		depots.add(new Depot(this, 20, 10, 5));
		depots.add(new Depot(this, 15, 10, 5));
		store = new Store(this, 3, 3);
		automaticRequests = new ArrayList<AutomaticRequest>();
		providers = new ArrayList<Provider>();
		Provider provider  = new Provider(this, "Proveedor Jefe", 4, 1);
		provider.addProduct(new Product("P 1", ProductType.DEHYDRATED));
		provider.addProduct(new Product("P 2", ProductType.CLEANING));
		provider.addProduct(new Product("P 3", ProductType.FRESH));
		providers.add(provider);

		//		Product p = provider.products.get(1);
		//		requestsProcessor.sendTask(provider, new InventoryRequest(p, 1));
		//		requestsProcessor.sendTask(provider, new InventoryRequest(p, 3));
		//		requestsProcessor.sendTask(provider, new InventoryRequest(p, 4));
		//		requestsProcessor.sendTask(provider, new InventoryRequest(p, 2));
		//		requestsProcessor.sendTask(provider, new InventoryRequest(p, 3));
		//		requestsProcessor.sendTask(provider, new InventoryRequest(p, 1));
		//		requestsProcessor.sendTask(provider, new InventoryRequest(p, 3, TaskRequestPriority.MIDDLE));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(0), 2, TaskRequestPriority.LOW));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 1, TaskRequestPriority.MIDDLE));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(2), 2, TaskRequestPriority.HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 2, TaskRequestPriority.VERY_HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 1, TaskRequestPriority.VERY_HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(2), 2, TaskRequestPriority.ALL_MIGHTY));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 2, TaskRequestPriority.LOW));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 4, TaskRequestPriority.HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 3, TaskRequestPriority.VERY_HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(2), 2, TaskRequestPriority.ALL_MIGHTY));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 1, TaskRequestPriority.LOW));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 2, TaskRequestPriority.HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 4, TaskRequestPriority.VERY_HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(2), 5, TaskRequestPriority.ALL_MIGHTY));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 2, TaskRequestPriority.LOW));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 2, TaskRequestPriority.HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 5, TaskRequestPriority.HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 3, TaskRequestPriority.VERY_HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(2), 2, TaskRequestPriority.ALL_MIGHTY));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 1, TaskRequestPriority.LOW));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(2), 2, TaskRequestPriority.HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 4, TaskRequestPriority.VERY_HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(2), 5, TaskRequestPriority.ALL_MIGHTY));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(2), 2, TaskRequestPriority.LOW));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 2, TaskRequestPriority.HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 4, TaskRequestPriority.VERY_HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(2), 5, TaskRequestPriority.ALL_MIGHTY));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 2, TaskRequestPriority.LOW));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 2, TaskRequestPriority.HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 5, TaskRequestPriority.HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 3, TaskRequestPriority.VERY_HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(2), 2, TaskRequestPriority.ALL_MIGHTY));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 1, TaskRequestPriority.LOW));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(2), 2, TaskRequestPriority.HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 4, TaskRequestPriority.VERY_HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(2), 5, TaskRequestPriority.ALL_MIGHTY));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 2, TaskRequestPriority.LOW));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 4, TaskRequestPriority.VERY_HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(2), 5, TaskRequestPriority.ALL_MIGHTY));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 2, TaskRequestPriority.LOW));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 2, TaskRequestPriority.HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 5, TaskRequestPriority.HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 3, TaskRequestPriority.VERY_HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(2), 2, TaskRequestPriority.ALL_MIGHTY));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 1, TaskRequestPriority.LOW));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(2), 2, TaskRequestPriority.HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 2, TaskRequestPriority.HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 5, TaskRequestPriority.HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 3, TaskRequestPriority.VERY_HIGH));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(2), 2, TaskRequestPriority.ALL_MIGHTY));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(1), 1, TaskRequestPriority.LOW));
		requestsProcessor.receiveTask(new InventoryRequest(provider.products.get(2), 2, TaskRequestPriority.HIGH));
	}

	protected void receiveStockTransfer(InventoryStockTransfer r) {
		Iterator<Depot> iter = depots.iterator();
		while(iter.hasNext()) {
			Depot depot = iter.next();
			if (depot.canMeetStockTransfer(r)) {
				outboundsProcessor.sendTask(depot, r);
				break;
			} else {
			}
		}
	}

	protected void receiveInventoryInbound(InventoryInbound r) {
		Iterator<Depot> iter = depots.iterator();
		while(iter.hasNext()) {
			Depot depot = iter.next();
			if (depot.canReceiveInbound(r)) {
				requestsProcessor.sendTask(depot, r);
				break;
			} else {
			}
		}
	}

	public void addInventoryEventListener(InventoryEventListener l) {
		listenerList.add(InventoryEventListener.class, l);
	}

	public void removeListDataListener(InventoryEventListener l) {
		listenerList.remove(InventoryEventListener.class, l);
	}

	public void addStorageStatusListener(StoreStatusListener l) {
		storeListenerList.add(StoreStatusListener.class, l);
	}

	public void removeListDataListener(StoreStatusListener l) {
		storeListenerList.remove(StoreStatusListener.class, l);
	}

	public void fireOnProductSelected(Product p) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length-2; i >= 0; i-=2) {
			if (listeners[i] == InventoryEventListener.class) {
				((InventoryEventListener)listeners[i+1]).onProductSelected(p);
			}
		}
		
	}
	public void fireStoreStatusUpdate() {
		Object[] listeners = storeListenerList.getListenerList();
		for (int i = listeners.length-2; i >= 0; i-=2) {
			if (listeners[i] == StoreStatusListener.class) {
				((StoreStatusListener)listeners[i+1]).onStoreStatusUpdate();
			}
		}
	}

	public void fireInventoryEvent() {
		fireInventoryEvent(null, null);
	}
	public void fireInventoryEvent(String s, PriorityBlockingQueue<TaskRequest> l) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = listeners.length-2; i >= 0; i-=2) {
			if (listeners[i] == InventoryEventListener.class) {
				((InventoryEventListener)listeners[i+1]).onTransferRequestUpdate();
			}
		}
	}

	public void addProvider(Provider o, Provider p) {
		addOrReplace(providers, o, p);
	}

	public <T> void addOrReplace(List<T> l, T o, T n) {
		int index = l.indexOf(o);
		if (o != null && index > -1) {
			l.remove(index);
			l.add(index, n);
		} else {
			l.add(n);
		}
	}

	public void addRequest(InventoryRequest original, InventoryRequest data) {
		requestsProcessor.sendTask(providers.get(0), data);
	}

	public void addStockTransfer(InventoryStockTransfer data) {
		store.sendTask(outboundsProcessor, data);
	}
	
	public void addAutomaticRequest(AutomaticRequest ar) {
		automaticRequests.add(ar);
	}

	public List<InventoryItem> getCurrentStockByProduct(Product p) {
		List<InventoryItem> entries = new ArrayList<InventoryItem>();
		Iterator<Depot> iter = depots.iterator();
		while(iter.hasNext()) {
			Depot depot = iter.next();
			List<InventoryItem> existent = depot.getCurrentStockByProduct(p);
			entries.addAll(existent);
		}
		return entries;
	}
	
	public boolean toogleActiveProduct(Product p) {
		if (p == selectedProduct) {
			fireOnProductSelected(null);
			selectedProduct = null;
			return false;
		}
		selectedProduct = p;
		fireOnProductSelected(p);
		return true;
	}
}
