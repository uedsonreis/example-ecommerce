package com.uedsonreis.ecommerce.controllers;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uedsonreis.ecommerce.entities.Item;
import com.uedsonreis.ecommerce.entities.SalesOrder;
import com.uedsonreis.ecommerce.entities.User;
import com.uedsonreis.ecommerce.services.SalesOrderService;
import com.uedsonreis.ecommerce.services.UserService;
import com.uedsonreis.ecommerce.utils.Util;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("sales/order")
public class SalesOrderController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private SalesOrderService salesOrderService;
	
	@PostMapping("/invoice")
	public ResponseEntity<Object> invoice(HttpServletRequest request, @RequestBody Collection<Item> cart) {
		User logged;
		try {
			logged = this.userService.getLoggedUser(request.getHeader(Util.AUTH));
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
		
		if (cart == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			
		} else {
			try {
				SalesOrder salesOrder = this.salesOrderService.invoice(cart, logged);
				return new ResponseEntity<>(salesOrder, HttpStatus.OK);
				
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	@GetMapping("/list")
	public ResponseEntity<Object> login(HttpServletRequest request) {
		User logged;
		try {
			logged = this.userService.getLoggedUser(request.getHeader(Util.AUTH));
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
		
		Collection<SalesOrder> salesOrders = this.salesOrderService.getSalesOrders(logged);
		
		if (salesOrders.size() < 1) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(salesOrders, HttpStatus.OK);
		}
	}

}