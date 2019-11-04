package com.uedsonreis.ecommerce.services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.stereotype.Service;

import com.uedsonreis.ecommerce.entities.Product;

@Service
public class ProductService {

	private Collection<Product> repository = new HashSet<>();
	
	public Boolean save(Product product) {
		return this.repository.add(product);
	}
	
	public Boolean remove(Product product) {
		
		if (this.repository.contains(product)) {
			return this.repository.remove(product);
		}
		
		return false;
	}
	
	public Collection<Product> getProducts() {
		return this.repository;
	}
	
}