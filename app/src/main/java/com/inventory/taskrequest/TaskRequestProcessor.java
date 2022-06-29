package com.inventory.taskrequest;

import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import javax.swing.event.EventListenerList;

class TaskRequestExecution implements Runnable, Comparable<TaskRequestExecution>{
	TaskRequest request;
	TaskExecutor executor;
	public static int count = 1;
	public int id;
	private volatile boolean suspended = false;
	volatile Timer timer;
	Thread current;

	public TaskRequestExecution(TaskExecutor e, TaskRequest t) {
		request = t;
		executor = e;
		id = count;
		count++;
	}
	
	public void suspend(){
		if (timer != null) {
			timer.cancel();
		}
		suspended = true;
	}

	public void resume(){
		suspended = false;
		synchronized (request) {
			request.notifyAll();
		}
		timer = new Timer();
		try { 
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (!suspended && !current.isInterrupted()) {
					current.interrupt();
				}
			}
		}, 250);
		} catch(Exception e) {
			System.out.println("EEE : " + e.getMessage());
		}
	}
	@Override  
	public void run() {  
		current = Thread.currentThread();
		if (!suspended) {
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					if (!suspended && !current.isInterrupted()) {
						current.interrupt();
					}
				}
			}, 250);
		}
		while(!Thread.currentThread().isInterrupted()){  
			if(!suspended){  
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			} else {
				//Has been suspended  
				try {                   
					while(suspended){  
						synchronized(request){  
							request.wait();  
						}                           
					}                       
				}  
				catch (InterruptedException e) {                    
				}             
			}                           
		}  
	}

	public TaskRequest getTaskRequest() {
		return request;
	}

	@Override
	public int compareTo(TaskRequestExecution o) {
		return request.compareTo(o.request);
	}

	public String toString() {
		return "Task #" + id;
	}
}

class FixedSizeQueue <T extends Comparable<T>> extends PriorityBlockingQueue<T> {
	private static final long serialVersionUID = 1L;
	int maxCapacity;
	public FixedSizeQueue(int m, boolean highPrioritFirst) {
		super(m, new Comparator<T>() {

			@Override
			public int compare(T o1, T o2) {
				if(highPrioritFirst) {
					return o1.compareTo(o2);
				} else {
					return o2.compareTo(o1);
				}
			}
		});
		maxCapacity = m;
	}
	@Override
	public boolean add(T e) {
		if (size() < maxCapacity) {
			return super.add(e);
		}
		return false;
	}
	
	public boolean canAdd(T e) {
		T head = peek();
		return e.compareTo(head) < 0;
	}
}

public class TaskRequestProcessor implements Runnable, TaskExecutor {
	ThreadPoolExecutor executor;
	ExecutorCompletionService<TaskRequestExecution> completionService;
    EventListenerList listenerList = new EventListenerList();
    
	int activeCapacity;
	int pausedCapacity;

	FixedSizeQueue<TaskRequestExecution> activeTasks;
	FixedSizeQueue<TaskRequestExecution> pausedTasks;
	PriorityBlockingQueue<TaskRequest> queue;

	public TaskRequestProcessor(int c, int p) {
		activeCapacity = c;
		pausedCapacity = p;

		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(activeCapacity + pausedCapacity + 1);
		completionService = new ExecutorCompletionService<TaskRequestExecution>(executor);
		queue = new PriorityBlockingQueue<TaskRequest>();
		activeTasks = new FixedSizeQueue<TaskRequestExecution>(c, true);
		pausedTasks = new FixedSizeQueue<TaskRequestExecution>(p, false);
		executor.execute(this);
	}
	
    public void addTaskRequestEventListener(TaskRequestEventListener l) {
        listenerList.add(TaskRequestEventListener.class, l);
    }

    public void removeTaskRequestEventListener(TaskRequestEventListener l) {
        listenerList.remove(TaskRequestEventListener.class, l);
    }
    
    public void fireTaskRequestEvent(TaskRequestEventListener.TaskRequestEventType type, TaskRequest tr) {
    	Object[] listeners = listenerList.getListenerList();
    	for (int i = listeners.length-2; i >= 0; i-=2) {
    		if (listeners[i] == TaskRequestEventListener.class) {
				((TaskRequestEventListener)listeners[i+1]).onTaskRequestEvent(this, type, tr);
    		}
    	}
    }

    public void fireTaskRequestAdd(TaskRequest tr) {
    	fireTaskRequestEvent(TaskRequestEventListener.TaskRequestEventType.ADD, tr);
    }
    
    public void fireTaskRequestStart(TaskRequest tr) {
    	fireTaskRequestEvent(TaskRequestEventListener.TaskRequestEventType.START, tr);
    }

    public void fireTaskRequestComplete(TaskRequest tr) {
    	fireTaskRequestEvent(TaskRequestEventListener.TaskRequestEventType.COMPLETE, tr);
    }

	private void reconcilieTaskQueues(TaskRequestExecution taskComplete) {
		activeTasks.remove(taskComplete);
		
		if (pausedTasks.size() > 0) {
			TaskRequestExecution pausedTask = pausedTasks.poll();
			activeTasks.add(pausedTask);
			pausedTask.resume();
		}
		fireTaskRequestComplete(taskComplete.request);
	}

	public synchronized boolean processTaskRequest(TaskRequest t) {
		if (activeTasks.size() < activeCapacity) {
			TaskRequestExecution task = new TaskRequestExecution(this, t);
			activeTasks.add(task);
			completionService.submit(task, task);
			return true;
		} else if (pausedTasks.size() < pausedCapacity) {
			TaskRequestExecution task = new TaskRequestExecution(this, t);
			if (activeTasks.canAdd(task)) {
				TaskRequestExecution pausedTask = activeTasks.poll();
				pausedTask.suspend();
				pausedTasks.add(pausedTask);
				completionService.submit(task, task);
				activeTasks.add(task);
			} else {
				pausedTasks.add(task);
				task.suspend();
				completionService.submit(task, task);
			}
			return true;
		}
		return false;
	}

	public void onTaskRequestComplete(TaskRequest t) {
	}

	@Override
	public void run() {
		while(true) {
			try {
				Future<TaskRequestExecution> taskWrapper = completionService.take();
				if (taskWrapper != null) {
					try {
						TaskRequestExecution task = taskWrapper.get();
						onTaskRequestComplete(task.request);
						reconcilieTaskQueues(task);
						TaskRequest nextTask = queue.peek();
						if (nextTask != null) {
							if (processTaskRequest(nextTask)) {
								queue.remove(nextTask);
								fireTaskRequestStart(nextTask);
							}
						}
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public boolean receiveTask(TaskRequest t) {
		boolean started = processTaskRequest(t);
		t.setStatus(TaskRequestStatus.RECEIVED);
		if (started) {
			fireTaskRequestStart(t);
		} else {
			queue.add(t);
			fireTaskRequestAdd(t);
		}
		return true;
	}

	@Override
	public boolean sendTask(TaskExecutor target, TaskRequest taskRequest) {
//		taskRequest.setStatus(TaskRequestStatus.COMPLETE);
		target.receiveTask(taskRequest);
		return true;
	}

	@Override
	public long getProcessingTime(TaskRequest tr) {
//		long time = ((long)(Math.random() * 1000)) * r.amount;
		long time = 4000;
		return time;
	}
	
	@Override
	public int countActiveTasks() {
		return activeTasks.size();
	}

	@Override
	public int countPausedTasks() {
		return pausedTasks.size();
		
	}

	@Override
	public int countQueue() {
		return queue.size();
	}
	
	public PriorityBlockingQueue<TaskRequest> getQueue() {
		return queue;
	}
}
