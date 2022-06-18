package com.inventory;

import java.util.Date;
import java.util.UUID;

enum TransferRequestStatus {
	CREATED,
	SENT,
	RECEIVED
}

enum TransferRequestPriority {
	NONE,
	LOW,
	MID,
	HIGH,
};



public class TransferRequest {
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
		return id;
	}
	public void setStatus(TransferRequestStatus s) {
		status = s;
	}
	
	public void setPriority(TransferRequestPriority p) {
		priority = p;
	}
}
