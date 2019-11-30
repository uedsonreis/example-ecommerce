package com.uedsonreis.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uedsonreis.ecommerce.entities.Customer;
import com.uedsonreis.ecommerce.entities.User;
import com.uedsonreis.ecommerce.repositories.CustomerRepository;
import com.uedsonreis.ecommerce.utils.Util;

@Service
public class CustomerService {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CustomerRepository repository;
	
	public Integer save(Customer customer) throws Exception {
		if (customer.getEmail() == null) return null;
		if (customer.getName() == null) return null;
		if (customer.getAge() == null) return null;
		if (customer.getUser() == null) return null;
		
		if (customer.getAge() < 18) throw new Exception(Util.getMsgCustomerMustBeAdult());
		if (customer.getUser().getPassword() == null) return null;
		
		Customer customerDB = this.repository.findByEmail(customer.getEmail());
		
		if (customerDB != null) throw new Exception(Util.getMsgUserAlreadyExists());
		
		customer.getUser().setLogin(customer.getEmail());
		this.userService.save(customer.getUser());
		
		customerDB = this.repository.save(customer);

		if (customerDB == null) return null;
		
		return customerDB.getId();
	}
	
	public Customer get(User user) {
		return this.repository.findByUser(user);
	}

	public Customer get(Integer id) {
		return this.repository.findById(id).get();
	}

}