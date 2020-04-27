package com.uedsonreis.ecommerce.api.controllers;

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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("sales/order")
public class SalesOrderController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private SalesOrderService salesOrderService;
	
	@ApiOperation(
		value = "It does registry a new sales order"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "It returns the new sales order"),
		@ApiResponse(code = 400, message = "Inconsistent data"),
		@ApiResponse(code = 401, message = "User must be logged in")
	})
	@PostMapping("/invoice")
	public ResponseEntity<Object> invoice(HttpServletRequest request, @RequestBody Collection<Item> cart) {
		User logged;
		try {
			logged = this.userService.getLoggedUser(request.getHeader(Util.AUTH));
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
		
		if (cart == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		} else {
			try {
				SalesOrder salesOrder = this.salesOrderService.invoice(cart, logged);
				return new ResponseEntity<>(salesOrder, HttpStatus.OK);
				
			} catch (Exception e) {
				return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		}
	}
	
	@ApiOperation(
		value = "It does return the sales order list."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "It returns the sales order list."),
		@ApiResponse(code = 204, message = "There is no sales order registered."),
		@ApiResponse(code = 401, message = "User must be logged in.")
	})
	@GetMapping("/list")
	public ResponseEntity<Object> login(HttpServletRequest request) {
		User logged;
		try {
			logged = this.userService.getLoggedUser(request.getHeader(Util.AUTH));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
		
		Collection<SalesOrder> salesOrders = this.salesOrderService.getSalesOrders(logged);
		
		if (salesOrders.size() < 1) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(salesOrders);
		}
	}

}