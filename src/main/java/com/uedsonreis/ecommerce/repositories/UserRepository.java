package com.uedsonreis.ecommerce.repositories;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uedsonreis.ecommerce.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

	public Collection<User> findAll();
	
	public User findByLogin(String login);
	
}