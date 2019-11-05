package com.uedsonreis.ecommerce.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.uedsonreis.ecommerce.entities.User;

@Service
public class UserService {

	private final List<User> repository = new ArrayList<>();
	
	public UserService() {
		User admin = new User();
		admin.setAdmin(true);
		admin.setLogin("admin");
		admin.setPassword("admin");
		this.repository.add(admin);
	}
	
	public User login(User user) {
		
		int index = this.repository.indexOf(user);
		if (index < 0) return null;
		
		User userDB = this.repository.get(index);
		
		if (userDB.getPassword().equals(user.getPassword())) {
			return userDB;
		}
		return null;
	}

}