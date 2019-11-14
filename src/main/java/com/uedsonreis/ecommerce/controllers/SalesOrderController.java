package com.uedsonreis.ecommerce.controllers;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uedsonreis.ecommerce.entities.Item;
import com.uedsonreis.ecommerce.entities.SalesOrder;
import com.uedsonreis.ecommerce.entities.User;
import com.uedsonreis.ecommerce.services.SalesOrderService;
import com.uedsonreis.ecommerce.utils.ReturnType;
import com.uedsonreis.ecommerce.utils.Util;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("sales/order")
public class SalesOrderController {

	@Autowired
	private SalesOrderService salesOrderService;
	
	@RequestMapping("/invoice")
	public ReturnType invoice(HttpSession httpSession) {
		
		@SuppressWarnings("unchecked")
		Map<Integer, Item> cart = (Map<Integer, Item>) httpSession.getAttribute(Util.CART);
		
		User logged = (User) httpSession.getAttribute(Util.LOGGED);
		
		ReturnType result = new ReturnType();
		
		if (cart == null) {
			result.setSuccess(false);
			result.setMessage(Util.getMsgNothingInCart());
			
		} else if (logged == null) {
			result.setSuccess(false);
			result.setMessage(Util.getMsgYouNeedLogIn());
			
		} else {
			try {
				SalesOrder salesOrder = this.salesOrderService.invoice(cart, logged);
				result.setData( salesOrder );
				
			} catch (Exception e) {
				result.setSuccess(false);
				result.setMessage(e.getMessage());
			}
		}
		return result;
	}
	
	@RequestMapping("/list")
	public ReturnType login(HttpSession httpSession) {
		
		User logged = (User) httpSession.getAttribute(Util.LOGGED);
		
		Collection<SalesOrder> salesOrders = this.salesOrderService.getSalesOrders(logged);
		
		ReturnType result = new ReturnType();
		
		if (salesOrders.size() < 1) {
			result.setSuccess(false);
			result.setMessage(Util.getMsgNoSalesOrder());
		} else {
			result.setData(salesOrders);
		}
		
		return result;
	}

}