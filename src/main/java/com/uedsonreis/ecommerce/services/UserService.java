package com.uedsonreis.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uedsonreis.ecommerce.entities.User;
import com.uedsonreis.ecommerce.repositories.UserRepository;
import com.uedsonreis.ecommerce.utils.security.TokenManager;

@Service
public class UserService {
	
	@Autowired
	private TokenManager tokenManager;

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
	
	public String generateToken(User user) {
		return this.tokenManager.generateToken(user.getLogin());
	}
	
	public User getLoggedUser(String authorization) throws Exception {
		String login = this.tokenManager.getLoggedUsername(authorization);
		return this.repository.findByLogin(login);
	}

}