package com.inventory.utils;

import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {
	long time;
	int maxCount;
	int count;
	Timer timer;
	Thread thread;
	String id;
	
	public Scheduler(Runnable target, String i) {
		timer = new Timer();
		thread = new Thread(target, i);
		id = i;
	}
	
	public void schedule(long time, int maxCount) {
		this.time = time;
		this.maxCount = maxCount;
		this.count = 0;
		if (this.count < maxCount) {
			this.runThread();
		}
	}

	private void runThread() {
		System.out.println(id + ": Running thread at count=" + count);
		synchronized(thread) {
			if (thread.getState() == Thread.State.NEW) {
				thread.start();
			} else {
				thread.resume();
			}
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Scheduler.this.timeOver();
				}
			}, time);
		}
	}

	private void timeOver() {
		System.out.println("SCHEDULER (" +id + ") : Time over in thread");
		timer.purge();
		count++;
		if (count < maxCount) {
			System.out.println("SCHEDULER (" + id + "): Continuing thread");
			runThread();
		} else {
			System.out.println("SCHEDULER (" + id + "): Interrupting thread");
			timer.cancel();
			thread.interrupt();
		}
	}
}
