package com.uedsonreis.ecommerce.utils;

public class Util {

	public static final String CART = "cart";
	public static final String AUTH = "Authorization";
	
	public static String getMsgCustomerAlreadyExists() {
		return "Customer email is already registered.";
	}
	
	public static String getMsgCustomerDoesntExists() {
		return "Customer doesn't exists.";
	}
	
	public static String getMsgCustomerMustBeAdult() {
		return "Customer must be an adult!";
	}
	
	public static String getMsgFactoryIsRequired() {
		return "Factory is required!";
	}
	
	public static String getMsgNoProductRegistered() {
		return "There is no product registered in database.";
	}
	
	public static String getMsgProductAmountIsRequired() {
		return "Product amount is required!";
	}
	
	public static String getMsgProductDoesntHaveAmount(String productName) {
		return "Product "+ productName +" doesn't have enough amount.";
	}
	
	public static String getMsgProductIdDoesntExist() {
		return "Product Id doesn't exists.";
	}
	
	public static String getMsgProductNameIsRequired() {
		return "Product name is required!";
	}
	
	public static String getMsgProductPriceIsRequired() {
		return "Product price is required!";
	}
	
	public static String getTokenExpired() {
		return "Token has expired";
	}
	
	public static String getUnableToGetToken() {
		return "Unable to get Token";
	}
	
}