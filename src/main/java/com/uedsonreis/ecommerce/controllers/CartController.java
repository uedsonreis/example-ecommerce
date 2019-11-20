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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uedsonreis.ecommerce.entities.Item;
import com.uedsonreis.ecommerce.entities.Product;
import com.uedsonreis.ecommerce.utils.Util;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("cart")
public class CartController {

	@GetMapping("/add")
	public void add(HttpSession httpSession,
			@RequestParam(value="productId") Integer productId,
			@RequestParam(value="price") Double price,
			@RequestParam(value="amount") Integer amount) {
		
		Product product = Product.builder().id(productId).build();
		Item item = Item.builder().price(price).amount(amount).product(product).build();
		
		this.add(httpSession, item);
	}
	
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
	
	@RequestMapping("/list")
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