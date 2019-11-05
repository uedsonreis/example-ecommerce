package com.uedsonreis.ecommerce.entities;

import java.util.Date;
import java.util.Set;

public class SalesOrder {

	private Double totalValue;
	private Customer customer;
	private Set<Item> items;
	private Date createdAt = new Date();
	
	public Double getTotalValue() {
		return totalValue;
	}
	public void setTotalValue(Double totalValue) {
		this.totalValue = totalValue;
	}
	
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public Set<Item> getItems() {
		return items;
	}
	public void setItems(Set<Item> items) {
		this.items = items;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	@Override
	public String toString() {
		return "SalesOrder [totalValue="+ totalValue +", customer=" + customer + ", createdAt=" + createdAt + "]";
	}

}