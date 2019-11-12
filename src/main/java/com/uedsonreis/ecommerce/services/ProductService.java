package com.uedsonreis.ecommerce.services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uedsonreis.ecommerce.entities.Product;
import com.uedsonreis.ecommerce.repositories.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private FactoryService factoryService;
	
	@Transactional
	public Integer save(Product product) {
		
		if (product.getName() == null) return null;
		if (product.getPrice() == null) return null;
		if (product.getAmount() == null) return null;
		if (product.getFactory() == null) return null;
		
		product.setFactory( this.factoryService.save( product.getFactory() ) );
		
		Product productDB = this.repository.save(product);
		
		if (productDB == null) return null;
		
		return productDB.getId();
	}
	
	public Product get(Integer id) {
		return this.repository.findById(id).get();
	}
	
	public Boolean remove(int id) {
		Product product = this.repository.findById(id).get();
		if (product == null) return false;
		this.repository.deleteById(id);
		return true;
	}
	
	public Collection<Product> getProducts() {
		return this.repository.findAll();
	}
	
}