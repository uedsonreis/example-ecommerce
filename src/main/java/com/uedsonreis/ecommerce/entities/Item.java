package com.uedsonreis.ecommerce.entities;

public class Item {

	private Double price;
	private Integer amount;
	private Product product;
	
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
	@Override
	public String toString() {
		return "Item [price=" + price + ", amount=" + amount + ", product=" + product + "]";
	}
	
}