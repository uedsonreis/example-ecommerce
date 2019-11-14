package com.uedsonreis.ecommerce.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.uedsonreis.ecommerce.utils.ReturnType;
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
	public ReturnType logged(HttpSession httpSession) {

		ReturnType result = new ReturnType();
		
		User logged = (User) httpSession.getAttribute(Util.LOGGED);
		
		if (logged == null) {
			result.setSuccess(false);
			result.setMessage(Util.getMsgNoUserLogged());
		} else {
			result.setData(logged.getLogin());
		}
		
		return result;
	}

	@GetMapping("/customer/add")
	public ReturnType addCustomer(HttpSession httpSession,
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
	public ReturnType addCustomer(HttpSession httpSession, @RequestBody Customer customer) {
		
		ReturnType result = new ReturnType();

		Integer id = this.customerService.save(customer);
		
		if (id == null) {
			result.setSuccess(false);
			result.setMessage(Util.getMsgEmailIsAlreadyRegistered());
		} else {
			result.setData(id);
			httpSession.setAttribute(Util.LOGGED, customer.getUser());
		}
		
		return result;
	}
	
	@GetMapping("/login")
	public ReturnType login(HttpSession httpSession,
			@RequestParam(value="login") String login,
			@RequestParam(value="password") String password) { // Only for class example, don't do it in real life!
		
		User user = User.builder().login(login).password(password).build();
		
		return this.login(httpSession, user);
	}
	
	@PostMapping("/login")
	public ReturnType login(HttpSession httpSession, @RequestBody User user) {
		
		User logged = this.userService.login(user);

		ReturnType result = new ReturnType();

		if (logged == null) {
			result.setSuccess(false);
			result.setMessage(Util.getMsgLoginOrPasswordInvalid());
		} else {
			httpSession.setAttribute(Util.LOGGED, logged);
			result.setData(logged.getLogin());
		}
		
		return result;
	}

}