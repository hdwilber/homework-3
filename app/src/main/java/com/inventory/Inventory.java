package com.inventory;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import javax.swing.event.EventListenerList;

interface InventoryEventListener extends EventListener {
	public void onTransferRequestUpdate(String target, List<TransferRequest> l);
}

class Store {
	List<TransferRequest> outboundOrders;
	public Store() {
		outboundOrders = new ArrayList<TransferRequest>();
	}

	public void receiveOutboundOrder(OutboundOrder o) {
		o.setStatus(TransferRequestStatus.RECEIVED);
		outboundOrders.add(o);
	}
}

public class Inventory {
	List<Depot> depots;
	List<Provider> providers;
	Store store;

	InventoryStatus status;
	List<TransferRequest> requests;
	List<TransferRequest> inboundOrders;
	List<TransferRequest> stockTransfers;
	List<TransferRequest> outboundOrders;
    EventListenerList listenerList = new EventListenerList();
    
	public Inventory() {
		depots = new ArrayList<Depot>();
		providers = new ArrayList<Provider>();
		store = new Store();
		
		Provider provider0 = new Provider(this, "Proveedor Jefe");
		provider0.addProduct(new Product("Producto 1", ProductType.DEHYDRATED));
		provider0.addProduct(new Product("Producto 2", ProductType.CLEANING));
		provider0.addProduct(new Product("Producto 3", ProductType.FRESH));

		providers.add(provider0);
		providers.add(new Provider(this, "Proveedor 1"));
		providers.add(new Provider(this, "Proveedor 2"));
		providers.add(new Provider(this, "Proveedor 3"));

		requests = new ArrayList<TransferRequest>();
//		requests.add(new TransferRequest());
//		requests.add(new TransferRequest());
//		requests.add(new TransferRequest());
		
		inboundOrders = new ArrayList<TransferRequest>();
//		inboundOrders.add(new TransferRequest());

		stockTransfers = new ArrayList<TransferRequest>();
//		stockTransfers.add(new TransferRequest());
//		stockTransfers.add(new TransferRequest());

		outboundOrders = new ArrayList<TransferRequest>();
//		outboundOrders.add(new TransferRequest());
//		outboundOrders.add(new TransferRequest());
//		outboundOrders.add(new TransferRequest());
//		outboundOrders.add(new TransferRequest());
//		outboundOrders.add(new TransferRequest());
//		outboundOrders.add(new TransferRequest());

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

    public void receiveStockTransfer(StockTransfer st) {
		st.setStatus(TransferRequestStatus.RECEIVED);

		// Dispatch items
		OutboundOrder result = new OutboundOrder(st);
		addOutboundOrder(null, result);
    }
    
    public void receiveInboundOrder(InboundOrder in) {
    	in.setStatus(TransferRequestStatus.RECEIVED);
    	
    	// Do fill depot
    }
	
	public void addRequest(Request o, Request p) {
		addOrReplace(requests, o, p);
		debug("REQUEST");
		fireInventoryEvent("TOP", requests);
		
		p.provider.receiveRequest(p);
		
	}
	
	public void addStockTransfer(StockTransfer o, StockTransfer p) {
		addOrReplace(stockTransfers, o, p);
		debug("STOCK TRANSFER");
		fireInventoryEvent("TOP", stockTransfers);
		
		receiveStockTransfer(p);
	}

	public void addInboundOrder(InboundOrder o, InboundOrder p) {
		addOrReplace(inboundOrders, o, p);
		debug("IN ORDRES");
		fireInventoryEvent("BOTTOM", inboundOrders);
		
		receiveInboundOrder(p);
	}

	public void addOutboundOrder(OutboundOrder o, OutboundOrder p) {
		addOrReplace(outboundOrders, o, p);
		debug("OUT ORDRES");
		fireInventoryEvent("BOTTOM", outboundOrders);
	}

	public void addProvider(Provider o, Provider p) {
		p.setInventory(this);
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

	public void debug(String step) {
		System.out.println("-----");
		System.out.println(step);
		System.out.println("PROVIDERS : " + providers.size());
		System.out.println("REQUESTS : " + requests.size());
		System.out.println("STOCK TRANFERS : " + stockTransfers.size());
		System.out.println("IN ORDERS : " + inboundOrders.size());
		System.out.println("OUT ORDERS : " + outboundOrders.size());
		System.out.println("-----");
	}
}
