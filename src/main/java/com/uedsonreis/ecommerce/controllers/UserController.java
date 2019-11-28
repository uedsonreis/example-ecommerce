package com.uedsonreis.ecommerce.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uedsonreis.ecommerce.entities.Customer;
import com.uedsonreis.ecommerce.entities.User;
import com.uedsonreis.ecommerce.services.CustomerService;
import com.uedsonreis.ecommerce.services.UserService;
import com.uedsonreis.ecommerce.utils.Util;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CustomerService customerService;
	
	@RequestMapping("/logged")
	public ResponseEntity<String> logged(HttpServletRequest request) {

		User logged;
		try {
			logged = this.userService.getLoggedUser(request.getHeader(Util.AUTH));
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<>(logged.getLogin(), HttpStatus.OK);
	}

	@GetMapping("/customer/add")
	public ResponseEntity<Object> addCustomer(
			@RequestParam(value="email") String email,
			@RequestParam(value="name") String name,
			@RequestParam(value="age") Integer age,
			@RequestParam(value="address") String address,
			@RequestParam(value="password") String password) {

		User user = User.builder().password(password).build();
		Customer customer = Customer.builder().email(email).name(name).age(age).address(address).user(user).build();
		
		return this.addCustomer(customer);
	}
	
	@PostMapping("/customer/add")
	public ResponseEntity<Object> addCustomer(@RequestBody Customer customer) {
		
		Integer id = null;
		try {
			id = this.customerService.save(customer);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		if (id == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			String token = this.userService.generateToken(customer.getUser());
			return new ResponseEntity<>(token, HttpStatus.OK);
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody User user) {
		
		User logged = this.userService.login(user);

		if (logged == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			String token = this.userService.generateToken(user);
			return new ResponseEntity<>(token, HttpStatus.OK);
		}
	}

}