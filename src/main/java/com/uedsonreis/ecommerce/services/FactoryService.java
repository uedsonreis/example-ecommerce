package com.uedsonreis.ecommerce.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uedsonreis.ecommerce.entities.Factory;
import com.uedsonreis.ecommerce.repositories.FactoryRepository;

@Service
public class FactoryService {

	@Autowired
	private FactoryRepository factoryRepository;
	
	public Factory save(Factory factory) {
		
		if (factory.getId() != null) {
			Optional<Factory> optional = this.factoryRepository.findById(factory.getId());
			
			if (optional.isPresent()) {
				return optional.get();
			}
		}

		Factory factoryDB = this.factoryRepository.findByName(factory.getName());
		
		if (factoryDB == null) {
			return this.factoryRepository.save(factory);
		} else {
			return factoryDB;
		}
	}
	
}