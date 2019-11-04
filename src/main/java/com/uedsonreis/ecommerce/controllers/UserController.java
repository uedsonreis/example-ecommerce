package com.uedsonreis.ecommerce.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uedsonreis.ecommerce.entities.Customer;
import com.uedsonreis.ecommerce.services.CustomerService;
import com.uedsonreis.ecommerce.utils.ReturnType;

@RestController
@RequestMapping("user")
public class UserController {
	
	@Autowired
	private CustomerService customerService;

	@RequestMapping("/customer/add")
	public ReturnType addCustomer(HttpSession httpSession,
			@RequestParam(value="email") String email,
			@RequestParam(value="name") String name,
			@RequestParam(value="age") Integer age,
			@RequestParam(value="address") String address) {
		
		Customer customer = new Customer();
		customer.setEmail(email);
		customer.setName(name);
		customer.setAge(age);
		customer.setAddress(address);
		
		ReturnType result = new ReturnType();
		
		Integer id = this.customerService.save(customer);
		
		if (id == null) {
			result.setSuccess(false);
			result.setMessage("This email is already registered.");
		} else {
			result.setData(id);
		}
		
		return result;
	}
}
