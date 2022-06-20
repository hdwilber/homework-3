package com.inventory;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import javax.swing.event.EventListenerList;

import com.inventory.utils.ProcessRequestChecker;

interface ProviderEventListener extends EventListener {
	public void onStatusUpdate();
}
public class Provider extends TransferRequestProcessor { //ProcessRequestChecker {
	Inventory inventory;
	String name;
	public List<Product> products;
	public PriorityBlockingQueue<TransferRequest> requestsQueue;
	int capacity;
    EventListenerList listenerList = new EventListenerList();

    public void addInventoryEventListener(ProviderEventListener l) {
        listenerList.add(ProviderEventListener.class, l);
    }

    public void removeListDataListener(ProviderEventListener l) {
        listenerList.remove(ProviderEventListener.class, l);
    }

    public void fireStatusUpdateEvent() {
    	Object[] listeners = listenerList.getListenerList();
    	for (int i = listeners.length-2; i >= 0; i-=2) {
    		if (listeners[i] == ProviderEventListener.class) {
    			((ProviderEventListener)listeners[i+1]).onStatusUpdate();
    		}
    	}
    }

	public Provider(String n, int c, int pc) {
		this(null, n, c, pc);
	}
	public Provider(Inventory i, String n, int c, int pc) {
		super(c, pc);
		name = n;
		products = new ArrayList<Product>();
		inventory = i;
		capacity = (int) (Math.round(Math.random() * 5) + 2);
		requestsQueue = new PriorityBlockingQueue<TransferRequest>(capacity);
	}
	
	public void addProduct(Product p) {
		products.add(p);
	}
	
	public void setInventory(Inventory i) {
		inventory = i;
	}
	
	int requestTotal = 0;
	public void receiveRequest(Request r) {
		r.setStatus(TransferRequestStatus.RECEIVED);
		requestTotal += r.amount;
		if (!processTransferRequest(r)) {
			requestsQueue.add(r);
		}
		inventory.fireInventoryEvent();
		System.out.println("Provider - Requests: " + requestTotal);
		fireStatusUpdateEvent();
	}
	
	public void onTaskComplete(TransferRequestExecutor task) {
		task.request.setStatus(TransferRequestStatus.PROCESSED);
		
		if (requestsQueue.size() > 0) {
			TransferRequest pausedRequest = requestsQueue.poll();
			if (!processTransferRequest(pausedRequest)) {
				requestsQueue.add(pausedRequest);
			} else {
			}
			inventory.fireInventoryEvent();
		}

		InboundOrder result = new InboundOrder((Request)task.request);
		inventory.receiveInboundOrder(result);
		fireStatusUpdateEvent();
	}

	public long getProcessingTime(Request r) {
//		long time = ((long)(Math.random() * 1000)) * r.amount;
		long time = 1000;
		return time;
	}
	
	public String toString() {
		return name;
	}
}