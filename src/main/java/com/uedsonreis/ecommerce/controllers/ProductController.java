package com.uedsonreis.ecommerce.controllers;

import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uedsonreis.ecommerce.entities.Factory;
import com.uedsonreis.ecommerce.entities.Product;
import com.uedsonreis.ecommerce.entities.User;
import com.uedsonreis.ecommerce.services.ProductService;
import com.uedsonreis.ecommerce.utils.ReturnType;
import com.uedsonreis.ecommerce.utils.Util;

@RestController
@RequestMapping("product")
public class ProductController {

	@Autowired
	ProductService productService;
	
	@GetMapping("/add")
	public ReturnType add(HttpSession httpSession,
			@RequestParam(value="name") String name,
			@RequestParam(value="factory") String factoryName,
			@RequestParam(value="price") Double price,
			@RequestParam(value="amount") Integer amount) {
		
		Factory factory = Factory.builder().name(factoryName).build();
		Product product = Product.builder().name(name).price(price).amount(amount).factory(factory).build();
		
		return this.add(httpSession, product);
	}
	
	@RequestMapping("/add")
	public ReturnType add(HttpSession httpSession, @RequestBody Product product) {
		
		ReturnType result = new ReturnType();
		
		User logged = (User) httpSession.getAttribute(Util.LOGGED);
		
		if (logged != null && logged.getAdmin()) {
			Integer productId = this.productService.save(product);
			result.setData(productId);
		} else {
			result.setSuccess(false);
			result.setMessage(Util.getMsgYouMustLogInAsAdm());
		}
		
		return result;
	}
	
	@RequestMapping("/remove")
	public ReturnType remove(HttpSession httpSession, @RequestParam(value="id") Integer id) {
		
		ReturnType result = new ReturnType();
		
		User logged = (User) httpSession.getAttribute(Util.LOGGED);
		
		if (logged != null && logged.getAdmin()) {
			if (!this.productService.remove(id)) {
				result.setSuccess(false);
				result.setMessage(Util.getMsgIdIsNotRegistered());
			}
		} else {
			result.setSuccess(false);
			result.setMessage(Util.getMsgYouMustLogInAsAdm());
		}
		
		return result;
	}
	
	@RequestMapping("/list")
	public ReturnType list() {
		
		ReturnType result = new ReturnType();
		
		Collection<Product> products = this.productService.getProducts();
		
		if (products == null || products.size() < 1) {
			result.setSuccess(false);
			result.setMessage(Util.getMsgNoProductRegistered());
		} else {
			result.setData(products);
		}
		
		return result;
	}

}