package com.uedsonreis.ecommerce.controllers;

import javax.servlet.http.HttpSession;

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
	public ResponseEntity<String> logged(HttpSession httpSession) {

		User logged = (User) httpSession.getAttribute(Util.LOGGED);
		
		if (logged == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(logged.getLogin(), HttpStatus.OK);
		}

	}

	@GetMapping("/customer/add")
	public ResponseEntity<Object> addCustomer(HttpSession httpSession,
			@RequestParam(value="email") String email,
			@RequestParam(value="name") String name,
			@RequestParam(value="age") Integer age,
			@RequestParam(value="address") String address,
			@RequestParam(value="password") String password) {

		User user = User.builder().password(password).build();
		Customer customer = Customer.builder().email(email).name(name).age(age).address(address).user(user).build();
		
		return this.addCustomer(httpSession, customer);
	}
	
	@PostMapping("/customer/add")
	public ResponseEntity<Object> addCustomer(HttpSession httpSession, @RequestBody Customer customer) {
		
		Integer id = null;
		try {
			id = this.customerService.save(customer);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		if (id == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			httpSession.setAttribute(Util.LOGGED, customer.getUser());
			return new ResponseEntity<>(id, HttpStatus.OK);
		}
	}
	
	@GetMapping("/login")
	public ResponseEntity<String> login(HttpSession httpSession,
			@RequestParam(value="login") String login,
			@RequestParam(value="password") String password) { // Only for class example, don't do it in real life!
		
		User user = User.builder().login(login).password(password).build();
		
		return this.login(httpSession, user);
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> login(HttpSession httpSession, @RequestBody User user) {
		
		User logged = this.userService.login(user);

		if (logged == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			httpSession.setAttribute(Util.LOGGED, logged);
			return new ResponseEntity<>(logged.getLogin(), HttpStatus.OK);
		}
	}

}