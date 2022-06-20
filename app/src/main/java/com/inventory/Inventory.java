package com.inventory;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import javax.swing.event.EventListenerList;

import com.inventory.utils.ProcessRequestChecker;

interface InventoryEventListener extends EventListener {
	public void onTransferRequestUpdate();
}
interface StoreStatusListener extends EventListener {
	public void onStoreStatusUpdate();
}

public class Inventory extends TransferRequestProcessor {
	Depot depot;
	List<Provider> providers;
	Store store;

	InventoryStatus status;
	List<TransferRequest> allEvents;
	PriorityBlockingQueue<TransferRequest> requests;
	PriorityBlockingQueue<TransferRequest> inboundOrders;
	PriorityBlockingQueue<TransferRequest> stockTransfers;
	PriorityBlockingQueue<TransferRequest> outboundOrders;
    EventListenerList listenerList = new EventListenerList();
    EventListenerList storeListenerList = new EventListenerList();
    
	public Inventory() {
		super(2, 1);
		depot = new Depot(10, 10, 4);
		store = new Store(this);
		providers = new ArrayList<Provider>();
		Provider provider  = new Provider(this, "Proveedor Jefe", 5, 5);
		provider.addProduct(new Product("Producto 1", ProductType.DEHYDRATED));
		provider.addProduct(new Product("Producto 2", ProductType.CLEANING));
		provider.addProduct(new Product("Producto 3", ProductType.FRESH));
		providers.add(provider);

		allEvents = new ArrayList<TransferRequest>();
		requests = new PriorityBlockingQueue<TransferRequest>();
		inboundOrders = new PriorityBlockingQueue<TransferRequest>();
		stockTransfers = new PriorityBlockingQueue<TransferRequest>();
		outboundOrders = new PriorityBlockingQueue<TransferRequest>();

		
		status = new InventoryStatus(this);

		addRequest(null, new Request(provider, provider.products.get(0), 2, TransferRequestPriority.LOW));
		addRequest(null, new Request(provider, provider.products.get(1), 1, TransferRequestPriority.MIDDLE));
		addRequest(null, new Request(provider, provider.products.get(2), 2, TransferRequestPriority.HIGH));
		addRequest(null, new Request(provider, provider.products.get(1), 2, TransferRequestPriority.VERY_HIGH));
		addRequest(null, new Request(provider, provider.products.get(1), 1, TransferRequestPriority.VERY_HIGH));
		addRequest(null, new Request(provider, provider.products.get(2), 2, TransferRequestPriority.ALL_MIGHTY));
		addRequest(null, new Request(provider, provider.products.get(1), 2, TransferRequestPriority.LOW));
		addRequest(null, new Request(provider, provider.products.get(1), 4, TransferRequestPriority.HIGH));
		addRequest(null, new Request(provider, provider.products.get(1), 3, TransferRequestPriority.VERY_HIGH));
		addRequest(null, new Request(provider, provider.products.get(2), 2, TransferRequestPriority.ALL_MIGHTY));
		addRequest(null, new Request(provider, provider.products.get(1), 1, TransferRequestPriority.LOW));
		addRequest(null, new Request(provider, provider.products.get(1), 2, TransferRequestPriority.HIGH));
		addRequest(null, new Request(provider, provider.products.get(1), 4, TransferRequestPriority.VERY_HIGH));
		addRequest(null, new Request(provider, provider.products.get(2), 5, TransferRequestPriority.ALL_MIGHTY));
		addRequest(null, new Request(provider, provider.products.get(1), 2, TransferRequestPriority.LOW));
		addRequest(null, new Request(provider, provider.products.get(1), 2, TransferRequestPriority.HIGH));


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
    public void fireInventoryEvent(String s, PriorityBlockingQueue<TransferRequest> l) {
    	Object[] listeners = listenerList.getListenerList();
    	for (int i = listeners.length-2; i >= 0; i-=2) {
    		if (listeners[i] == InventoryEventListener.class) {
    			((InventoryEventListener)listeners[i+1]).onTransferRequestUpdate();
    		}
    	}
    }

    public void receiveStockTransfer(StockTransfer st) {
		st.setStatus(TransferRequestStatus.RECEIVED);
		stockTransfers.add(st);
		fireInventoryEvent("BOTTOM", stockTransfers);
    }
    
    int totalIn = 0;
    public void receiveInboundOrder(InboundOrder in) {
    	totalIn += in.amount;
    	System.out.println("INVEORY - RECEIVED:  " + totalIn);
    	in.setStatus(TransferRequestStatus.RECEIVED);

    	fireStoreStatusUpdate();
		if (!processTransferRequest(in)) {
			inboundOrders.add(in);
		}
    	fireStoreStatusUpdate();
		fireInventoryEvent();
    }
    
    public void addProvider(Provider o, Provider p) {
    	addOrReplace(providers, o, p);
    }
	
	public void addRequest(Request o, Request p) {
		allEvents.add(p);
		p.provider.receiveRequest(p);
	}
	
	public void addInboundOrder(InboundOrder o, InboundOrder p) {
		inboundOrders.add(p);
		fireInventoryEvent("BOTTOM", inboundOrders);
		
		receiveInboundOrder(p);
	}

	public void addOutboundOrder(OutboundOrder o, OutboundOrder p) {
		outboundOrders.add(p);
		fireInventoryEvent("BOTTOM", outboundOrders);
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

	public long getProcessingTime(InboundOrder io) {
//		long time = ((long)(Math.random() * 1000)) * io.amount;
		long time = 2500;
		return time;
	}

	public long getProcessingTime(StockTransfer io) {
//		long time = ((long)(Math.random() * 500)) * io.amount;
		long time = 2500;
		return time;
	}

	@Override
	public void onTaskComplete(TransferRequestExecutor task) {
		task.request.setStatus(TransferRequestStatus.PROCESSING);
		if (inboundOrders.size() > 0) {
			TransferRequest pausedRequest = inboundOrders.poll();
			if (!processTransferRequest(pausedRequest)) {
				inboundOrders.add(pausedRequest);
			} else {
			}
			fireInventoryEvent();
		}
		fireStoreStatusUpdate();
		depot.receiveInboundOrder((InboundOrder)task.request);

//		InboundOrder result = new InboundOrder((Request)task.request);
//		inventory.receiveInboundOrder(result);
		
	}
}
