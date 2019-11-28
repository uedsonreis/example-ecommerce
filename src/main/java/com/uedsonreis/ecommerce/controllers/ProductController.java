package com.uedsonreis.ecommerce.controllers;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uedsonreis.ecommerce.entities.Factory;
import com.uedsonreis.ecommerce.entities.Product;
import com.uedsonreis.ecommerce.entities.User;
import com.uedsonreis.ecommerce.services.ProductService;
import com.uedsonreis.ecommerce.services.UserService;
import com.uedsonreis.ecommerce.utils.Util;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("product")
public class ProductController {

	@Autowired
	UserService userService;
	
	@Autowired
	ProductService productService;
	
	@GetMapping("/add")
	public ResponseEntity<Object> add(HttpServletRequest request,
			@RequestParam(value="name") String name,
			@RequestParam(value="factory") String factoryName,
			@RequestParam(value="price") Double price,
			@RequestParam(value="amount") Integer amount) {
		
		Factory factory = Factory.builder().name(factoryName).build();
		Product product = Product.builder().name(name).price(price).amount(amount).factory(factory).build();
		
		return this.add(request, product);
	}
	
	@PostMapping("/add")
	public ResponseEntity<Object> add(HttpServletRequest request, @RequestBody Product product) {
		
		User logged;
		try {
			logged = this.userService.getLoggedUser(request.getHeader(Util.AUTH));
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
		
		if (logged == null || (!logged.getAdmin())) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		Integer productId = null;
		
		try {
			productId = this.productService.save(product);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		if (productId == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(productId, HttpStatus.OK);
		}
	}
	
	@DeleteMapping("/{id}/remove")
	public ResponseEntity<Object> remove(HttpServletRequest request, @PathVariable(value="id") Integer id) {
		
		User logged;
		try {
			logged = this.userService.getLoggedUser(request.getHeader(Util.AUTH));
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		if (logged == null || (!logged.getAdmin())) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		if (this.productService.remove(id)) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping("/list")
	public ResponseEntity<Object> list() {
		
		Collection<Product> products = this.productService.getProducts();
		
		if (products == null || products.size() < 1) {
			return new ResponseEntity<>(Util.getMsgNoProductRegistered(), HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(products, HttpStatus.OK);
		}
	}

}