package com.uedsonreis.ecommerce.api.controllers;

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
import org.springframework.web.bind.annotation.RestController;

import com.uedsonreis.ecommerce.entities.Product;
import com.uedsonreis.ecommerce.entities.User;
import com.uedsonreis.ecommerce.services.ProductService;
import com.uedsonreis.ecommerce.services.UserService;
import com.uedsonreis.ecommerce.utils.Util;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("product")
public class ProductController {

	@Autowired
	UserService userService;
	
	@Autowired
	ProductService productService;
	
	@ApiOperation(
		value = "It add a new product in database"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "It returns the id of the new product"),
		@ApiResponse(code = 400, message = "Inconsistent data"),
		@ApiResponse(code = 401, message = "User must be a admin to be able to add a new product")
	})
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
	
	@ApiOperation(
		value = "It does delete a product in the database"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Product was delete"),
		@ApiResponse(code = 204, message = "There is no product with this ID"),
		@ApiResponse(code = 401, message = "User must be a admin to be able to remove a product")
	})
	@DeleteMapping("/{id}/remove")
	public ResponseEntity<Object> remove(HttpServletRequest request, @PathVariable(value="id") Integer id) {
		
		User logged;
		try {
			logged = this.userService.getLoggedUser(request.getHeader(Util.AUTH));
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
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
	
	@ApiOperation(
		value = "It get the registered product list."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, response = Product[].class, message = "It returns the product list."),
		@ApiResponse(code = 204, message = "There is no registered product.")
	})
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