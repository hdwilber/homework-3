package com.inventory;

import com.inventory.taskrequest.InventoryOutbound;
import com.inventory.taskrequest.InventoryStockTransfer;
import com.inventory.taskrequest.TaskRequest;
import com.inventory.taskrequest.TaskRequestProcessor;

class Store extends TaskRequestProcessor {
	Inventory inventory;
	public Store(Inventory i, int c, int cp) {
		super(c, cp);
		inventory = i;
	}

	public void addStockTransfer(InventoryStockTransfer st) {
		sendTask(inventory.outboundsProcessor, st);
	}

	public long getProcessingTime(TaskRequest tr) {
		InventoryOutbound ir = (InventoryOutbound)tr;
		long time = ((long)(Math.random() * 1000)) * ir.getAmount();
		return time;
	}
}

