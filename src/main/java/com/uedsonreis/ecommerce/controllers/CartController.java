package com.uedsonreis.ecommerce.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uedsonreis.ecommerce.entities.Item;
import com.uedsonreis.ecommerce.entities.Product;
import com.uedsonreis.ecommerce.utils.ReturnType;
import com.uedsonreis.ecommerce.utils.Util;

@RestController
@RequestMapping("cart")
public class CartController {

	@GetMapping("/add")
	public ReturnType add(HttpSession httpSession,
			@RequestParam(value="productId") Integer productId,
			@RequestParam(value="price") Double price,
			@RequestParam(value="amount") Integer amount) {
		
		Item item = new Item();
		item.setPrice(price);
		item.setAmount(amount);
		
		Product product = new Product();
		product.setId(productId);
		
		return this.add(httpSession, item);
	}
	
	@PostMapping("/add")
	public ReturnType add(HttpSession httpSession, @RequestBody Item item) {

		ReturnType result = new ReturnType();
			
		Map<Integer, Item> cart = this.getShoppingCart(httpSession);
		
		Item itemInCart = cart.get(item.getProduct().getId());
		
		if (itemInCart == null) {
			cart.put(item.getProduct().getId(), item);
		} else {
			itemInCart.setAmount( itemInCart.getAmount() + item.getAmount() );
		}
		
		httpSession.setAttribute(Util.CART, cart);
		
		return result;
	}
	
	@RequestMapping("/list")
	public ReturnType list(HttpSession httpSession) {
		ReturnType result = new ReturnType();
		
		Map<Integer, Item> cart = this.getShoppingCart(httpSession);
		
		if (cart.isEmpty()) {
			result.setSuccess(false);
			result.setMessage(Util.getMsgNothingInCart());
		} else {
			result.setData(cart.values());
		}
		
		return result;
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