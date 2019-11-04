package com.uedsonreis.ecommerce.controllers;

import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uedsonreis.ecommerce.entities.Factory;
import com.uedsonreis.ecommerce.entities.Product;
import com.uedsonreis.ecommerce.services.ProductService;
import com.uedsonreis.ecommerce.utils.ReturnType;

@RestController
@RequestMapping("product")
public class ProductController {

	@Autowired
	ProductService productService;
	
	@RequestMapping("/add")
	public ReturnType add(HttpSession httpSession,
			@RequestParam(value="name") String name,
			@RequestParam(value="factory") String factoryName,
			@RequestParam(value="price") Double price,
			@RequestParam(value="amount") Integer amount) {
		
		Factory factory = new Factory();
		factory.setName(factoryName);

		Product product = new Product();
		product.setName(name);
		product.setPrice(price);
		product.setAmount(amount);
		product.setFactory(factory);
		
		ReturnType result = new ReturnType();
		
		Integer productId = this.productService.save(product);
		result.setData(productId);
		
		return result;
	}
	
	@RequestMapping("/remove")
	public ReturnType remove(@RequestParam(value="id") Integer id) {
		ReturnType result = new ReturnType();
		
		if (!this.productService.remove(id)) {
			result.setSuccess(false);
			result.setMessage("This ID is not registered in database.");
		}
		
		return result;
	}
	
	@RequestMapping("/list")
	public ReturnType login() {
		
		ReturnType result = new ReturnType();
		
		Collection<Product> products = this.productService.getProducts();
		
		if (products == null || products.size() < 1) {
			result.setSuccess(false);
			result.setMessage("There is no product registered in database.");
		} else {
			result.setData(products);
		}
		
		return result;
	}

}