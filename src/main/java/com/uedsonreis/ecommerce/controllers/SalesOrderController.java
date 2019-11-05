package com.uedsonreis.ecommerce.controllers;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uedsonreis.ecommerce.entities.Item;
import com.uedsonreis.ecommerce.entities.SalesOrder;
import com.uedsonreis.ecommerce.services.SalesOrderService;
import com.uedsonreis.ecommerce.utils.ReturnType;
import com.uedsonreis.ecommerce.utils.Util;


@RestController
@RequestMapping("sales/order")
public class SalesOrderController {

	@Autowired
	private SalesOrderService salesOrderService;
	
	@RequestMapping("/invoice")
	public ReturnType invoice(HttpSession httpSession, @RequestParam(value="customerId") Integer customerId) {
		
		@SuppressWarnings("unchecked")
		Map<Integer, Item> cart = (Map<Integer, Item>) httpSession.getAttribute(Util.CART);
		
		ReturnType result = new ReturnType();
		
		if (cart == null) {
			result.setSuccess(false);
			result.setMessage("There is no item in your shopping cart.");
		} else {
			Integer id = this.salesOrderService.invoice(cart, customerId);
			
			if (id == null) {
				result.setSuccess(false);
				result.setMessage("There are something wrong with the items or the customer id.");
			} else {
				result.setData(id);
			}
		}
		
		return result;
	}
	
	@RequestMapping("/list")
	public ReturnType login(HttpSession httpSession) {
		
		ReturnType result = new ReturnType();
		
		Collection<SalesOrder> salesOrders = this.salesOrderService.getSalesOrders();
		
		if (salesOrders.size() < 1) {
			result.setSuccess(false);
			result.setMessage("There is no sales order registered in database for you.");
		} else {
			result.setData(salesOrders);
		}
		
		return result;
	}

}