package com.uedsonreis.ecommerce.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.uedsonreis.ecommerce.entities.Product;

@Service
public class ProductService {

	private final List<Product> repository = new ArrayList<>();
	
	public Integer save(Product product) {
		if (this.repository.add(product)) {
			return this.repository.indexOf(product);
		}
		return null;
	}
	
	public Boolean remove(int id) {
		return (this.repository.remove(id) != null);
	}
	
	public Collection<Product> getProducts() {
		return this.repository;
	}
	
}