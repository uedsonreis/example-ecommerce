package com.uedsonreis.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uedsonreis.ecommerce.entities.User;
import com.uedsonreis.ecommerce.repositories.UserRepository;
import com.uedsonreis.ecommerce.security.TokenManager;
import com.uedsonreis.ecommerce.utils.Util;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private TokenManager tokenManager;

	@Autowired
	private UserRepository repository;
	
	public void save(User user) throws IllegalStateException {
		if (user.getAdmin() == null) user.setAdmin(false);
		
		String encodePassword = this.passwordEncoder.encode(user.getPassword());
		user.setPassword( encodePassword );
		
		try {
			this.repository.save(user);
		} catch (IllegalStateException e) {
			throw new IllegalStateException(Util.getMsgUserAlreadyExists());
		}
	}
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.repository.findByLogin(username);
		if (user == null) {
			throw new UsernameNotFoundException("User unregistered!");
		}
		return user;
	}
	
	public String generateToken(User user) {
		return this.tokenManager.generateToken(user.getLogin());
	}
	
	public User getLoggedUser(String authorization) throws Exception {
		String login = this.tokenManager.getLoggedUsername(authorization);
		return this.repository.findByLogin(login);
	}

}