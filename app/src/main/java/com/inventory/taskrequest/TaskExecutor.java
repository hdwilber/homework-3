package com.inventory.taskrequest;

public interface TaskExecutor {
	public boolean receiveTask(TaskRequest t);
	boolean sendTask(TaskExecutor target, TaskRequest taskRequest);
	public long getProcessingTime(TaskRequest tr);
	public int countActiveTasks();
	public int countPausedTasks();
	public int countQueue();
}