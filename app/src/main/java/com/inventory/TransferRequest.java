package com.inventory;

import java.util.Date;
import java.util.UUID;

enum TransferRequestStatus {
	CREATED,
	SENT,
	RECEIVED,
	PROCESSING,
	PROCESSED,
	REJECTED
}

enum TransferRequestPriority {
	NONE(0, "NINGUNA"),
	LOW(1, "BAJA"),
	MIDDLE(2, "MEDIA"),
	HIGH(3, "ALTA"),
	VERY_HIGH(4, "MUY ALTA"),
	ALL_MIGHTY(5, "IMPARABLE");

	private final String name;
	private final int value;

	TransferRequestPriority(int v, String p) {
		value = v;
		name = p;
	}
	public String toString() {
		return name;
	}
	public int getValue() {
		return value;
	}
};

public abstract class TransferRequest implements Comparable<TransferRequest> {
	String id;
	Date creation_date;
	TransferRequestStatus status;
	TransferRequestPriority priority;

	public TransferRequest() {
		this(TransferRequestPriority.NONE);
	}
	public TransferRequest(TransferRequestPriority p) {
		id = UUID.randomUUID().toString();
		creation_date = new Date();
		status = TransferRequestStatus.CREATED;
		priority = p;
	}
	public String toString() {
		return id + " (" + priority +")";
	}
	public void setStatus(TransferRequestStatus s) {
		status = s;
	}
	
	public void setPriority(TransferRequestPriority p) {
		priority = p;
	}

	@Override
	public int compareTo(TransferRequest o) {
		return o.priority.getValue() - priority.getValue();
	}
	
	public abstract String getLabel();
}
