package com.uedsonreis.ecommerce.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uedsonreis.ecommerce.entities.Customer;
import com.uedsonreis.ecommerce.entities.User;
import com.uedsonreis.ecommerce.services.CustomerService;
import com.uedsonreis.ecommerce.services.UserService;
import com.uedsonreis.ecommerce.utils.Util;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("user")
public class UserController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CustomerService customerService;
	
	@ApiOperation(
		value = "It returns the username for a given token in headers",
		response = User.class
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "It returns the logged username"),
		@ApiResponse(code = 204, message = "No one is logged")
	})
	@GetMapping("/logged")
	public ResponseEntity<String> logged(HttpServletRequest request) {
		User logged;
		try {
			logged = this.userService.getLoggedUser(request.getHeader(Util.AUTH));
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<>(logged.getLogin(), HttpStatus.OK);
	}
	
	@ApiOperation(
		value = "It does registry a new customer user and return his token",
		response = String.class
	)
	@ApiResponses(value = {
		@ApiResponse(code = 201, message = "Customer created with success"),
		@ApiResponse(code = 400, message = "Some wrong with the parameters")
	})
	@PostMapping("/customer/add")
	public ResponseEntity<Object> addCustomer(@RequestBody Customer customer) {
		
		final String password = customer.getUser().getPassword(); 
		
		Integer id = null;
		try {
			id = this.customerService.save(customer);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		if (id == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			User user = customer.getUser();
			
			this.authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(user.getLogin(), password, user.getAuthorities())
			);
			
			String token = this.userService.generateToken(user);
			return new ResponseEntity<>(token, HttpStatus.CREATED);
		}
	}
	
	@ApiOperation(
		value = "It does login with an username and a password"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully login"),
		@ApiResponse(code = 401, message = "You are not authorized to login in this system")
	})
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody User user) {
		try {
			this.authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword(), user.getAuthorities())
			);
			
			String token = this.userService.generateToken(user);
			return new ResponseEntity<>(token, HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
	}

}