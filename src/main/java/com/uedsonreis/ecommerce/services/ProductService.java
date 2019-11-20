package com.uedsonreis.ecommerce.services;

import java.util.Collection;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uedsonreis.ecommerce.entities.Product;
import com.uedsonreis.ecommerce.repositories.ProductRepository;
import com.uedsonreis.ecommerce.utils.Util;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private FactoryService factoryService;
	
	@Transactional
	public Integer save(Product product) throws Exception {
		
		if (product.getName() == null) throw new Exception(Util.getMsgProductNameIsRequired());
		if (product.getPrice() == null) throw new Exception(Util.getMsgProductPriceIsRequired());
		if (product.getAmount() == null) throw new Exception(Util.getMsgProductAmountIsRequired());
		if (product.getFactory() == null) throw new Exception(Util.getMsgFactoryIsRequired());
		
		product.setFactory( this.factoryService.save( product.getFactory() ) );
		
		Product productDB = this.repository.save(product);
		
		if (productDB == null) return null;
		
		return productDB.getId();
	}
	
	public Product get(Integer id) {
		return this.repository.findById(id).get();
	}
	
	public Boolean remove(int id) {
		Optional<Product> product = this.repository.findById(id);
		if (!product.isPresent()) return false;
		
		this.repository.deleteById(id);
		return true;
	}
	
	public Collection<Product> getProducts() {
		return this.repository.findAll();
	}
	
}