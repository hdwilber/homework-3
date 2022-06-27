package com.inventory;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import javax.swing.event.EventListenerList;

import com.inventory.taskrequest.*;

interface InventoryEventListener extends EventListener {
	public void onTransferRequestUpdate();
}
interface StoreStatusListener extends EventListener {
	public void onStoreStatusUpdate();
}

public class Inventory {
	Depot depot;
	List<Provider> providers;
	Store store;

	EventListenerList listenerList = new EventListenerList();
	EventListenerList storeListenerList = new EventListenerList();

	public TaskRequestProcessor requestsProcessor = new TaskRequestProcessor(4, 1) {
		public long getProcessingTime(TaskRequest io) {
			InventoryRequest ir = (InventoryRequest)io;
			long time = ((long)(Math.random() * 1000)) * ir.getAmount();
			return time;
		}
		@Override
		public void onTaskRequestComplete(TaskRequest r) {
			depot.receiveInventoryInbound((InventoryInbound)r);
		}
	};

	public TaskRequestProcessor outboundsProcessor = new TaskRequestProcessor(4, 1) {
		public long getProcessingTime(TaskRequest io) {
			InventoryStockTransfer ir = (InventoryStockTransfer)io;
			long time = ((long)(Math.random() * 1000)) * ir.getAmount();
			return time;
		}
		@Override
		public void onTaskRequestComplete(TaskRequest r) {
			List<InventoryOutbound> res = depot.receiveStockTransfer((InventoryStockTransfer)r);
			if (res != null) {
				res.forEach(order -> {
					sendTask(store, order);
				});
			}
		}
	};

	public Inventory() {
		super();
		depot = new Depot(10, 10, 4);
		store = new Store(this, 3, 3);
		providers = new ArrayList<Provider>();
		Provider provider  = new Provider(this, "Proveedor Jefe", 3, 1);
		provider.addProduct(new Product("Producto 1", ProductType.DEHYDRATED));
		provider.addProduct(new Product("Producto 2", ProductType.CLEANING));
		provider.addProduct(new Product("Producto 3", ProductType.FRESH));
		providers.add(provider);

		//		Product p = provider.products.get(1);
		//		requestsProcessor.sendTask(provider, new InventoryRequest(p, 1));
		//		requestsProcessor.sendTask(provider, new InventoryRequest(p, 3));
		//		requestsProcessor.sendTask(provider, new InventoryRequest(p, 4));
		//		requestsProcessor.sendTask(provider, new InventoryRequest(p, 2));
		//		requestsProcessor.sendTask(provider, new InventoryRequest(p, 3));
		//		requestsProcessor.sendTask(provider, new InventoryRequest(p, 1));
		//		requestsProcessor.sendTask(provider, new InventoryRequest(p, 3, TaskRequestPriority.MIDDLE));
//		requestsProcessor.sendTask(provider, new InventoryRequest(provider.products.get(0), 2, TaskRequestPriority.LOW));
//		requestsProcessor.sendTask(provider, new InventoryRequest(provider.products.get(1), 1, TaskRequestPriority.MIDDLE));
//		requestsProcessor.sendTask(provider, new InventoryRequest(provider.products.get(2), 2, TaskRequestPriority.HIGH));
//		requestsProcessor.sendTask(provider, new InventoryRequest(provider.products.get(1), 2, TaskRequestPriority.VERY_HIGH));
//		requestsProcessor.sendTask(provider, new InventoryRequest(provider.products.get(1), 1, TaskRequestPriority.VERY_HIGH));
//		requestsProcessor.sendTask(provider, new InventoryRequest(provider.products.get(2), 2, TaskRequestPriority.ALL_MIGHTY));
//		requestsProcessor.sendTask(provider, new InventoryRequest(provider.products.get(1), 2, TaskRequestPriority.LOW));
//		requestsProcessor.sendTask(provider, new InventoryRequest(provider.products.get(1), 4, TaskRequestPriority.HIGH));
//		requestsProcessor.sendTask(provider, new InventoryRequest(provider.products.get(1), 3, TaskRequestPriority.VERY_HIGH));
//		requestsProcessor.sendTask(provider, new InventoryRequest(provider.products.get(2), 2, TaskRequestPriority.ALL_MIGHTY));
//		requestsProcessor.sendTask(provider, new InventoryRequest(provider.products.get(1), 1, TaskRequestPriority.LOW));
//		requestsProcessor.sendTask(provider, new InventoryRequest(provider.products.get(1), 2, TaskRequestPriority.HIGH));
//		requestsProcessor.sendTask(provider, new InventoryRequest(provider.products.get(1), 4, TaskRequestPriority.VERY_HIGH));
//		requestsProcessor.sendTask(provider, new InventoryRequest(provider.products.get(2), 5, TaskRequestPriority.ALL_MIGHTY));
//		requestsProcessor.sendTask(provider, new InventoryRequest(provider.products.get(1), 2, TaskRequestPriority.LOW));
		requestsProcessor.sendTask(provider, new InventoryRequest(provider.products.get(1), 25, TaskRequestPriority.HIGH));
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
}
