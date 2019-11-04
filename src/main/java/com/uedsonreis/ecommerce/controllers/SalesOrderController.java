package com.uedsonreis.ecommerce.controllers;

import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uedsonreis.ecommerce.entities.SalesOrder;
import com.uedsonreis.ecommerce.services.SalesOrderService;
import com.uedsonreis.ecommerce.utils.ReturnType;


@RestController
@RequestMapping("sales/order")
public class SalesOrderController {

	@Autowired
	private SalesOrderService salesOrderService;
	
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