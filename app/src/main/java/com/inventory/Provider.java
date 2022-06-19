package com.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.PriorityBlockingQueue;

import com.inventory.utils.ProcessRequestChecker;
import com.inventory.utils.Scheduler;

public class Provider extends ProcessRequestChecker {
	Inventory inventory;
	String name;
	List<Product> products;
	public PriorityBlockingQueue<TransferRequest> requestsQueue;
	int capacity;

	public Provider(String n) {
		this(null, n);
	}
	public Provider(Inventory i, String n) {
		name = n;
		products = new ArrayList<Product>();
		inventory = i;
		capacity = (int) (Math.round(Math.random() * 5) + 2);
		requestsQueue = new PriorityBlockingQueue<TransferRequest>(capacity);
		startChecker();
	}
	
	public void addProduct(Product p) {
		products.add(p);
	}
	
	public void setInventory(Inventory i) {
		inventory = i;
	}
	
	public void receiveRequest(Request r) {
		r.setStatus(TransferRequestStatus.RECEIVED);
		requestsQueue.add(r);
		inventory.fireInventoryEvent();
		continueChecker();
	}

	public long getProcessingTime(Request r) {
//		long time = ((long)(Math.random() * 1000)) * r.amount;
		long time = 2000;
		return time;
	}
	
	public String toString() {
		return name;
	}
	@Override
	public synchronized void processRequest() {
		try {
			Request request = (Request)requestsQueue.element();
			try {
				Thread.sleep(getProcessingTime(request));
				request.setStatus(TransferRequestStatus.PROCESSED);

				InboundOrder result = new InboundOrder(request);
				requestsQueue.poll();
				inventory.receiveInboundOrder(result);

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
		return requestsQueue.size() == 0;
	}
}