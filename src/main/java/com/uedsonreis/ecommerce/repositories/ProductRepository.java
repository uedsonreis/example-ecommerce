package com.uedsonreis.ecommerce.repositories;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uedsonreis.ecommerce.entities.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {

	public Collection<Product> findAll();
	
}