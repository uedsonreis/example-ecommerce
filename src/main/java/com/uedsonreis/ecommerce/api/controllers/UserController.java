package com.uedsonreis.ecommerce.api.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
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

import com.uedsonreis.ecommerce.api.dto.customer.CustomerInput;
import com.uedsonreis.ecommerce.api.dto.user.UserInput;
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
	private ModelMapper modelMapper;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CustomerService customerService;
	
	@ApiOperation(
		value = "It returns the username for a given token in headers",
		response = String.class
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
		notes = "The user must be an adult (age equals 18 or greater).",
		response = String.class
	)
	@ApiResponses(value = {
		@ApiResponse(code = 201, response = String.class, message = "It returns a token that allows to access the system."),
		@ApiResponse(code = 400, message = "Some wrong with the parameters")
	})
	@PostMapping("/customer/add")
	public ResponseEntity<Object> addCustomer(@Valid @RequestBody CustomerInput customer) {
		Customer customer2 = this.modelMapper.map(customer, Customer.class);
		
		final String password = customer2.getUser().getPassword(); 
		
		Integer id = null;
		try {
			id = this.customerService.save(customer2);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		if (id == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			User user = customer2.getUser();
			
			this.authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(user.getLogin(), password, user.getAuthorities())
			);
			
			String token = this.userService.generateToken(user);
			return new ResponseEntity<>(token, HttpStatus.CREATED);
		}
	}
	
	@ApiOperation(
		value = "It does login with an username and a password and returns a token.",
		response = String.class
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, response = String.class, message = "It returns a token that allows to access the system."),
		@ApiResponse(code = 401, message = "Login or password is invalid."),
	})
	@PostMapping("/login")
	public ResponseEntity<String> login(@Valid @RequestBody UserInput user) {
		User user2 = this.modelMapper.map(user, User.class);
		try {
			this.authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						user2.getLogin(),
						user2.getPassword(),
						user2.getAuthorities()
				)
			);
			
			String token = this.userService.generateToken(user2);
			return new ResponseEntity<>(token, HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
	}

}