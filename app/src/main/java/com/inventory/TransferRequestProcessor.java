package com.inventory;

import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

class TransferRequestExecutor extends Thread implements Comparable<TransferRequestExecutor> {
	TransferRequest request;
	public TransferRequestExecutor(TransferRequest r) {
		request = r;
	}

	@Override
	public void run() {
		System.out.println("Process request: " + request.id);
		try {
			Thread.sleep(Math.round(Math.random() * 10000));
		System.out.println("Process request after await " + request.id);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int compareTo(TransferRequestExecutor o) {
		return request.compareTo(o.request);
	}
}
public class TransferRequestProcessor implements Runnable {
	ThreadPoolExecutor executor;
	int capacity;
	int pausedCapacity;
	PriorityBlockingQueue<TransferRequestExecutor> queue;
	PriorityBlockingQueue<TransferRequestExecutor> pausedQueue;
	
	public TransferRequestProcessor(int c, int p) {
		capacity = c;
		pausedCapacity = p;
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(capacity);
		queue = new PriorityBlockingQueue<TransferRequestExecutor>(c);
		pausedQueue = new PriorityBlockingQueue<TransferRequestExecutor>(p);
	}
	
	public boolean processTransferRequest(TransferRequest r) {
		if (executor.getPoolSize() >= capacity) {
			return false;
		} else {
			TransferRequestExecutor task = new TransferRequestExecutor(r);
			if (executor.getPoolSize() < capacity) {
				queue.add(task);
				executor.execute(task);
				return true;
			}
		}
		return false;
	}
	
	public boolean hasQueuedProcesses() {
		return queue.size() > 0 || pausedQueue.size() > 0;
	}

	public void run() {
		while(hasQueuedProcesses()) {

		}
	}
	
//	public boolean processRequest(TransferRequest t) {
//		System.out.println("The processors: " + tasks.toString());
//		if (tasks.size() >= capacity) {
//			System.out.println("IT MAX CAP");
//		}
//			TransferRequestExecutor task = new TransferRequestExecutor(t);
//			tasks.add(task);
//			executor.execute(task);
//			return true;
//	}
////	
//	public static void main(String args[]) {
//		TransferRequestProcessor pro = new TransferRequestProcessor(5);
//		pro.processRequest(new TransferRequest());
//		pro.processRequest(new TransferRequest());
//		pro.processRequest(new TransferRequest());
//		pro.processRequest(new TransferRequest());
//		pro.processRequest(new TransferRequest());
//		pro.processRequest(new TransferRequest());
//		pro.processRequest(new TransferRequest());
//		pro.processRequest(new TransferRequest());
//		
//		System.out.println("TOTAL " + pro.executor.getPoolSize());
//		System.out.println("TOTAL " + pro.tasks.size());
//	}
}
