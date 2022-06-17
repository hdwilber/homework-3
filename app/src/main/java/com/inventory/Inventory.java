package com.inventory;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import javax.swing.event.EventListenerList;

interface InventoryEventListener extends EventListener {
	public void onTransferRequestUpdate(String target, List<TransferRequest> l);
}

public class Inventory {
	List<Depot> depots;
	List<Provider> providers;
	InventoryStatus status;
	List<TransferRequest> requests;
	List<TransferRequest> inboundOrders;
	List<TransferRequest> stockTransfers;
	List<TransferRequest> outboundOrders;
    EventListenerList listenerList = new EventListenerList();
	
	public Inventory() {
		depots = new ArrayList<Depot>();
		providers = new ArrayList<Provider>();
		
		Provider provider0 = new Provider("Proveedor Jefe");
		provider0.addProduct(new Product("Producto 1", ProductType.DEHYDRATED));
		provider0.addProduct(new Product("Producto 2", ProductType.CLEANING));
		provider0.addProduct(new Product("Producto 3", ProductType.FRESH));

		providers.add(provider0);
		providers.add(new Provider("Proveedor 1"));
		providers.add(new Provider("Proveedor 2"));
		providers.add(new Provider("Proveedor 3"));

		requests = new ArrayList<TransferRequest>();
		requests.add(new TransferRequest());
		requests.add(new TransferRequest());
		requests.add(new TransferRequest());
		
		inboundOrders = new ArrayList<TransferRequest>();
		inboundOrders.add(new TransferRequest());

		stockTransfers = new ArrayList<TransferRequest>();
		stockTransfers.add(new TransferRequest());
		stockTransfers.add(new TransferRequest());

		outboundOrders = new ArrayList<TransferRequest>();
		outboundOrders.add(new TransferRequest());
		outboundOrders.add(new TransferRequest());
		outboundOrders.add(new TransferRequest());
		outboundOrders.add(new TransferRequest());
		outboundOrders.add(new TransferRequest());
		outboundOrders.add(new TransferRequest());

		status = new InventoryStatus(this);
	}

    public void addInventoryEventListener(InventoryEventListener l) {
        listenerList.add(InventoryEventListener.class, l);
    }

    public void removeListDataListener(InventoryEventListener l) {
        listenerList.remove(InventoryEventListener.class, l);
    }

    public void fireInventoryEvent(String s, List<TransferRequest> l) {
    	Object[] listeners = listenerList.getListenerList();
    	for (int i = listeners.length-2; i >= 0; i-=2) {
    		if (listeners[i] == InventoryEventListener.class) {
    			((InventoryEventListener)listeners[i+1]).onTransferRequestUpdate(s,  l);
    		}
    	}
    }

	
	public void addRequest(Request o, Request p) {
		addOrReplace(requests, o, p);
		fireInventoryEvent("TOP", requests);
	}
	
	public void addStockTransfers(StockTransfer o, StockTransfer p) {
		
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

	public void debug() {
		System.out.println("Total PROVIDERS : " + providers.size());
		System.out.println("Total REQUESTS : " + requests.size());
	}
}
