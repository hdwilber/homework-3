package com.inventory;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import javax.swing.event.EventListenerList;

import com.inventory.utils.ProcessRequestChecker;

interface InventoryEventListener extends EventListener {
	public void onTransferRequestUpdate();
}

public class Inventory {
	List<Depot> depots;
	List<Provider> providers;
	Store store;

	InventoryStatus status;
	List<TransferRequest> allEvents;
	PriorityBlockingQueue<TransferRequest> requests;
	PriorityBlockingQueue<TransferRequest> inboundOrders;
	PriorityBlockingQueue<TransferRequest> stockTransfers;
	PriorityBlockingQueue<TransferRequest> outboundOrders;
    EventListenerList listenerList = new EventListenerList();
    
	ProcessRequestChecker inboundChecker = new ProcessRequestChecker() {
		@Override
		public void processRequest() {
			try {
				InboundOrder inboundOrder = (InboundOrder)inboundOrders.element();
				try {
					Thread.sleep(getProcessingTime(inboundOrder));
					inboundOrder.setStatus(TransferRequestStatus.PROCESSED);
					// DEPOSIT
					allEvents.add(inboundOrders.poll());
					fireInventoryEvent();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch(Exception error) {
				pauseChecker();
			}
		}

		@Override
		public boolean shouldPauseChecker() {
			return inboundOrders.size() == 0;
		}
		
	};
	ProcessRequestChecker stockTransferChecker = new ProcessRequestChecker() {
		@Override
		public void processRequest() {
			try {
				StockTransfer stockTransfer = (StockTransfer)stockTransfers.element();
				try {
					Thread.sleep(getProcessingTime(stockTransfer));
					stockTransfer.setStatus(TransferRequestStatus.PROCESSED);

					// DEPOSIT
					allEvents.add(stockTransfers.poll());
					fireInventoryEvent("BOTTOM", stockTransfers);
					stockTransfers.poll();
					OutboundOrder result = new OutboundOrder(stockTransfer);
					store.receiveOutboundOrder(result);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch(Exception error) {
				pauseChecker();
			}
		}

		@Override
		public boolean shouldPauseChecker() {
			return stockTransfers.size() == 0;
		}
		
	};
	public Inventory() {
		depots = new ArrayList<Depot>();
		store = new Store(this);
		providers = new ArrayList<Provider>();
		Provider provider  = new Provider(this, "Proveedor Jefe");
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
		inboundChecker.startChecker();
		stockTransferChecker.startChecker();
	}

    public void addInventoryEventListener(InventoryEventListener l) {
        listenerList.add(InventoryEventListener.class, l);
    }

    public void removeListDataListener(InventoryEventListener l) {
        listenerList.remove(InventoryEventListener.class, l);
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
		stockTransferChecker.continueChecker();
    }
    
    public void receiveInboundOrder(InboundOrder in) {
    	in.setStatus(TransferRequestStatus.RECEIVED);
    	inboundOrders.add(in);
		fireInventoryEvent();

		inboundChecker.continueChecker();
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
		long time = 2000;
		return time;
	}

	public long getProcessingTime(StockTransfer io) {
//		long time = ((long)(Math.random() * 500)) * io.amount;
		long time = 2000;
		return time;
	}
}
