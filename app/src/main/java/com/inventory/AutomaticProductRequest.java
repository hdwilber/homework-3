package com.inventory;

import com.inventory.product.Product;
import com.inventory.taskrequest.TaskRequestPriority;

public class AutomaticProductRequest {
	Product product;
	Provider provider;
	int minumumAmount;
	int amount;
	TaskRequestPriority priority;
	
	public AutomaticProductRequest(Provider p, Product product, TaskRequestPriority pri, int min, int a) {
		this.product = product;
		provider = p;
		minumumAmount = min;
		amount = a;
		priority = pri;
	}
	

}
