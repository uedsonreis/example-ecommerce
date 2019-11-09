package com.uedsonreis.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uedsonreis.ecommerce.entities.User;
import com.uedsonreis.ecommerce.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	
	public User login(User user) {
		
		User userDB = this.repository.findByLogin(user.getLogin());
		
		if (userDB == null) return null;
		
		if (!userDB.getPassword().equals(user.getPassword())) {
			return null;
		}
		
		return userDB;
	}

}