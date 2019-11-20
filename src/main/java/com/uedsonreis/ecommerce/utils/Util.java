package com.uedsonreis.ecommerce.utils;

public class Util {

	public static final String CART = "cart";
	public static final String LOGGED = "logged";
	
	public static String getMsgCustomerDoesntExists() {
		return "Customer doesn't exists.";
	}
	
	public static String getMsgItemInvalid() {
		return "There are something wrong with the items.";
	}
	
	public static String getMsgNoProductRegistered() {
		return "There is no product registered in database.";
	}
	
	public static String getMsgNoSalesOrder() {
		return "There is no sales order registered in database for you.";
	}
	
}