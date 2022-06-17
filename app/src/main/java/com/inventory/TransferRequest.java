package com.inventory;

import java.util.Date;
import java.util.UUID;

enum TransferRequestStatus {
	CREATED,
	SENT,
	RECEIVED
}

public class TransferRequest {
	String id;
	Date creation_date;
	TransferRequestStatus status;

	public TransferRequest() {
		id = UUID.randomUUID().toString();
		creation_date = new Date();
		status = TransferRequestStatus.CREATED;
	}
	public String toString() {
		return id;
	}
}
