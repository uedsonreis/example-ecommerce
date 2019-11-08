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
			product.setId( this.repository.size() - 1 );
			return product.getId();
		}
		return null;
	}
	
	public Product get(Integer id) {
		if (id < 0 || id >= this.repository.size()) return null;
		return this.repository.get(id);
	}
	
	public Boolean remove(int id) {
		return (this.repository.remove(id) != null);
	}
	
	public Collection<Product> getProducts() {
		return this.repository;
	}
	
}