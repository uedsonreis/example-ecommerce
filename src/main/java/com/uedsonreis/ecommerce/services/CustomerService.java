package com.uedsonreis.ecommerce.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uedsonreis.ecommerce.entities.Customer;
import com.uedsonreis.ecommerce.entities.User;

@Service
public class CustomerService {

	@Autowired
	private UserService userService;
	
	private final List<Customer> repository = new ArrayList<>();
	
	public Integer save(Customer customer) {
		if (customer.getAge() < 18) return null;
		
		int index = this.repository.indexOf(customer);
		
		if (index >= 0) return null;
		
		customer.getUser().setLogin(customer.getEmail());
		
		if (!this.userService.add(customer.getUser())) {
			return null;
		}

		if (!this.repository.add(customer)) {
			return null;
		}
		
		customer.setId( this.repository.size() - 1 );
		return customer.getId();
	}
	
	public Customer get(User user) {
		
		for (Customer customer: this.repository) {
			if (customer.getUser().equals(user)) {
				return customer;
			}
		}
		
		return null;
	}

	public Customer get(Integer id) {
		if (id < 0 || id >= this.repository.size()) return null;
		return this.repository.get(id);
	}

}