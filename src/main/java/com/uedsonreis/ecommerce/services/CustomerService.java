package com.uedsonreis.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uedsonreis.ecommerce.entities.Customer;
import com.uedsonreis.ecommerce.entities.User;
import com.uedsonreis.ecommerce.repositories.CustomerRepository;

@Service
public class CustomerService {
	
	@Autowired
	private CustomerRepository repository;
	
	public Integer save(Customer customer) {
		if (customer.getEmail() == null) return null;
		if (customer.getName() == null) return null;
		if (customer.getAge() == null) return null;
		if (customer.getUser() == null) return null;
		
		if (customer.getAge() < 18) return null;
		if (customer.getUser().getPassword() == null) return null;
		
		Customer customerDB = this.repository.findByEmail(customer.getEmail());
		
		if (customerDB != null) return null;
		
		customer.getUser().setLogin(customer.getEmail());
		customer.getUser().setAdmin(false);
		
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