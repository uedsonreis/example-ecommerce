package com.uedsonreis.ecommerce.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uedsonreis.ecommerce.entities.Customer;
import com.uedsonreis.ecommerce.entities.User;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {

	public Customer findByEmail(String email);
	public Customer findByUser(User user);

}