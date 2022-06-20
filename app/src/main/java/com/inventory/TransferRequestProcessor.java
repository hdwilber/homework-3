package com.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

class TransferRequestExecutor extends Thread implements Comparable<TransferRequestExecutor>, Callable<TransferRequestExecutor> {
	public static int count = 0;
	TransferRequest request;
	int id;
	boolean isPaused = false;
	boolean isStarted = false;
	public TransferRequestExecutor(TransferRequest r) {
		request = r;
		count++;
		id = count;
	}
	
	public String toString() {
		return "Task: " + id + " (" + (request.priority) + ")";
	}

	@Override
	public void run() {
		try {
			Thread.sleep(Math.round(Math.random() * 5000));
//			System.out.println("---");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setPaused(boolean b) {
		isPaused = b;
	}

	@Override
	public int compareTo(TransferRequestExecutor o) {
		if (isPaused) {
			return request.compareTo(o.request);
		} else {
			return o.request.compareTo(request);
		}
	}

	@Override
	public TransferRequestExecutor call() throws Exception {
		isStarted = true;
		try {
			Thread.sleep(Math.round(10000 * Math.random()));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}
}

class SortedList<T extends Comparable<T>> extends ArrayList<T> {
	private static final long serialVersionUID = 1L;
	
	@Override
	public synchronized boolean add(T item) {
		boolean result = super.add(item);
		Collections.sort(this);
		return result;
	}
	public synchronized T poll() {
		T result = get(0);
		remove(0);
		return result;
	}
	
	public synchronized boolean remove(T item) {
		return super.remove(item);
	}
	public synchronized T head() {
		return get(0);
	}
}

public abstract class TransferRequestProcessor implements Runnable {
	ThreadPoolExecutor executor;
	ExecutorCompletionService<TransferRequestExecutor> completionService;
	int capacity;
	int pausedCapacity;
	volatile SortedList<TransferRequestExecutor> queue;
	volatile SortedList<TransferRequestExecutor> pausedQueue;
	volatile SortedList<TransferRequestExecutor> awaitingQueue;
	
	public TransferRequestProcessor(int c, int p) {
		capacity = c;
		pausedCapacity = p;
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(capacity + pausedCapacity +1);
		completionService = new ExecutorCompletionService<TransferRequestExecutor>(executor);
		queue = new SortedList<TransferRequestExecutor>();
		pausedQueue = new SortedList<TransferRequestExecutor>();
		awaitingQueue = new SortedList<TransferRequestExecutor>();
		
		executor.execute(this);
	}
	
	public abstract void onTaskComplete(TransferRequestExecutor task);

	public synchronized void reconcilieQueues(TransferRequestExecutor completedTask) {
		boolean res = queue.remove(completedTask);
		if (res) {
//			System.out.println(completedTask.toString() + " TERMINADO y removido: " + res); 
		} else {
			pausedQueue.remove(completedTask);
//			System.out.println(completedTask.toString() + " TERMINADO Y removido: " +  pausedQueue.remove(completedTask) + " EN PAUSED "); 
		}
		if (pausedQueue.size() > 0) {
			TransferRequestExecutor pausedTask = pausedQueue.poll();
//			System.out.println(pausedTask + " RESUMIENDO");
			pausedTask.setPaused(false);
			queue.add(pausedTask);
			if (pausedTask.isStarted) {
				pausedTask.resume();
			} else {
				completionService.submit(pausedTask);
			}
		}
		onTaskComplete(completedTask);

//		System.out.println("------------------");
//		System.out.println("Queue: " + queue.size() + " - " +  queue);
//		System.out.println("Paused: " + pausedQueue.size() + " - " +  pausedQueue);
//		System.out.println("------------------");
	}
	
	public synchronized boolean processTransferRequest(TransferRequest r) {
		if (queue.size() <= (capacity -1)) {
			TransferRequestExecutor task = new TransferRequestExecutor(r);
//			System.out.println(task.toString() + " ADDED ");
			queue.add(task);
			completionService.submit(task);
			return true;
		} else if (pausedQueue.size() <= (pausedCapacity - 1)){
//			System.out.println("There is a: " + queue.size());
			TransferRequestExecutor taskToPause = queue.head();
			if (r.compareTo(taskToPause.request) < 0) {
//				System.out.println("Higher priority. "+ taskToPause.toString() + " PAUSING ");
				taskToPause.suspend();
				taskToPause.setPaused(true);
				queue.remove(taskToPause);
				pausedQueue.add(taskToPause);
				TransferRequestExecutor task = new TransferRequestExecutor(r);
//				System.out.println(task.toString() + " ADDED REPLACING " + taskToPause);
				queue.add(task);
				completionService.submit(task);
			} else {
				TransferRequestExecutor task = new TransferRequestExecutor(r);
				task.setPaused(true);
//				System.out.println(task.toString() + " ADDED PAUSED ");
				pausedQueue.add(task);
			}
			return true;
		}
//		System.out.println("REJECTING - QUEUES FULL");
		return false;
	}

	@Override
	public void run() {
		while(true) {
			try {
				Future<TransferRequestExecutor> taskWrapper = completionService.take();
				if (taskWrapper != null) {
					try {
						TransferRequestExecutor task = taskWrapper.get();
						reconcilieQueues(task);
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}
			} catch (InterruptedException e1) {
//				System.out.println("INTERRUPTED");
				e1.printStackTrace();
			}
		}
	}
}
