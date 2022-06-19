package com.inventory;

import java.util.concurrent.PriorityBlockingQueue;

import com.inventory.utils.ProcessRequestChecker;

class Store extends ProcessRequestChecker {
	PriorityBlockingQueue<TransferRequest> outboundOrders;
	Inventory inventory;
	public Store(Inventory i) {
		inventory = i;
		outboundOrders = new PriorityBlockingQueue<TransferRequest>();
		startChecker();
	}

	public void receiveOutboundOrder(OutboundOrder o) {
		o.setStatus(TransferRequestStatus.RECEIVED);
		outboundOrders.add(o);
		inventory.fireInventoryEvent();
		continueChecker();
	}

	public void addStockTransfer(StockTransfer st) {
		inventory.receiveStockTransfer(st);
	}

	@Override
	public void processRequest() {
		try {
			OutboundOrder outboundOrder = (OutboundOrder)outboundOrders.element();
			try {
				inventory.fireInventoryEvent();
				Thread.sleep(getProcessingTime(outboundOrders));
				outboundOrder.setStatus(TransferRequestStatus.PROCESSED);
				outboundOrders.poll();
				inventory.fireInventoryEvent();

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch(Exception error) {
			pauseChecker();
		}

	}

	private long getProcessingTime(PriorityBlockingQueue<TransferRequest> o) {
		return 2500;
	}

	@Override
	public boolean shouldPauseChecker() {
		return outboundOrders.size() == 0;
	}
}

