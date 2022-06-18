package com.inventory.utils;

public abstract class ProcessRequestChecker implements Runnable {
	long time;
	Thread thread;
	boolean suspended;

	public ProcessRequestChecker() {
		thread = new Thread(this);
	}
	public void startChecker() {
		thread.start();
	}
	public void pauseChecker() {
		thread.suspend();
	}
	public void continueChecker() {
		thread.resume();
	}

	public abstract void processRequest();
	public abstract boolean shouldPauseChecker();
	
	public void run() {
		boolean stopped = false;
		while(!stopped) {
			try {
				processRequest();
				if(shouldPauseChecker()) {
					thread.suspend();
				}
			} catch(Exception error) {
				error.printStackTrace();
				stopped = true;
			}
		}
	}
}
