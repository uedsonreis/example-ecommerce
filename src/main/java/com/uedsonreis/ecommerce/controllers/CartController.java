package com.uedsonreis.ecommerce.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uedsonreis.ecommerce.entities.Item;
import com.uedsonreis.ecommerce.utils.Util;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("cart")
public class CartController {

	@ApiOperation(
		value = "It does add a new item in the cart"
	)
	@PostMapping("/add")
	public void add(HttpSession httpSession, @RequestBody Item item) {

		Map<Integer, Item> cart = this.getShoppingCart(httpSession);
		
		Item itemInCart = cart.get(item.getProduct().getId());
		
		if (itemInCart == null) {
			cart.put(item.getProduct().getId(), item);
		} else {
			itemInCart.setAmount( itemInCart.getAmount() + item.getAmount() );
		}
		
		httpSession.setAttribute(Util.CART, cart);
	}
	
	@ApiOperation(
		value = "It does return the items in the cart"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "It returns the items in the cart"),
		@ApiResponse(code = 204, message = "There is no item in the cart")
	})
	@GetMapping("/list")
	public ResponseEntity<Object> list(HttpSession httpSession) {
		Map<Integer, Item> cart = this.getShoppingCart(httpSession);
		
		if (cart.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(cart.values(), HttpStatus.OK);
		}
	}
	
	private Map<Integer, Item> getShoppingCart(HttpSession httpSession) {
		@SuppressWarnings("unchecked")
		Map<Integer, Item> cart = (Map<Integer, Item>) httpSession.getAttribute(Util.CART);

		if (cart == null) {
			return new HashMap<>();
		} else {
			return cart;
		}
	}
	
}