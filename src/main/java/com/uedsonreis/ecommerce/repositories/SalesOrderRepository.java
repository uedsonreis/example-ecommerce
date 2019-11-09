package com.uedsonreis.ecommerce.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uedsonreis.ecommerce.entities.Customer;
import com.uedsonreis.ecommerce.entities.SalesOrder;

@Repository
public interface SalesOrderRepository extends CrudRepository<SalesOrder, Integer> {

	public List<SalesOrder> findAllByCustomer(Customer customer);
	
}