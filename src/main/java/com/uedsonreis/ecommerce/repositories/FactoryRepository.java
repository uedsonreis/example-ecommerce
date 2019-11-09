package com.uedsonreis.ecommerce.repositories;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uedsonreis.ecommerce.entities.Factory;

@Repository
public interface FactoryRepository extends CrudRepository<Factory, Integer> {

	public Collection<Factory> findAll();
	
	public Factory findByName(String name);
	
}