package com.inventory.taskrequest;

import java.awt.Font;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public abstract class TaskRequest implements Comparable<TaskRequest>{

	static int count = 1;
	int id;
	protected TaskRequestPriority priority;
	protected TaskRequestStatus status;
	protected Date creation_date;
	
	public TaskRequest() {
		this(TaskRequestPriority.NONE);
	}
	public TaskRequest(TaskRequestPriority p) {
		setPriority(p);
		creation_date = new Date();
		setStatus(TaskRequestStatus.CREATED);
		id = count;
		count++;
	}

	@Override
	public int compareTo(TaskRequest tr) {
		return tr.getPriority().getValue() - getPriority().getValue();
	}

	public TaskRequestPriority getPriority() {
		return priority;
	}

	public void setPriority(TaskRequestPriority p) {
		priority = p;
	}

	public TaskRequestStatus getStatus() {
		return status;
	}

	public void setStatus(TaskRequestStatus s) {
		status = s;
	}
	
	public String getLabel() {
		return "TaskRequest ["+priority+"] #" + id;
	}
	public String toString() {
		return "TaskRequest ["+priority+"] #" + id;
	}

	public abstract void setContentInfo(JPanel panel, ImageIcon icon, Font font);
}