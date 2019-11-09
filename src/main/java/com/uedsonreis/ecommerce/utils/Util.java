package com.uedsonreis.ecommerce.utils;

public class Util {

	public static final String CART = "cart";
	public static final String LOGGED = "logged";
	
	public static String getMsgCustomerDoesntExists() {
		return "Customer doesn't exists.";
	}
	
	public static String getMsgEmailIsAlreadyRegistered() {
		return "This email is already registered.";
	}
	
	public static String getMsgIdIsNotRegistered() {
		return "This ID is not registered in database.";
	}

	public static String getMsgItemInvalid() {
		return "There are something wrong with the items.";
	}
	
	public static String getMsgLoginOrPasswordInvalid() {
		return "Login or password is invalid.";
	}
	
	public static String getMsgNoProductRegistered() {
		return "There is no product registered in database.";
	}
	
	public static String getMsgNoSalesOrder() {
		return "There is no sales order registered in database for you.";
	}
	
	public static String getMsgNothingInCart() {
		return "There is no item in your shopping cart.";
	}
	
	public static String getMsgNoUserLogged() {
		return "There is no user logged.";
	}
	
	public static String getMsgYouMustLogInAsAdm() {
		return "You must log in as an Administrator User to be able to request that.";
	}
	
	public static String getMsgYouNeedLogIn() {
		return "You must log in to be able to request that.";
	}

}