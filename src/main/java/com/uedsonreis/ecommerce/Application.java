package com.uedsonreis.ecommerce;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.uedsonreis.ecommerce.entities.Factory;
import com.uedsonreis.ecommerce.entities.Product;
import com.uedsonreis.ecommerce.entities.User;
import com.uedsonreis.ecommerce.repositories.FactoryRepository;
import com.uedsonreis.ecommerce.repositories.UserRepository;
import com.uedsonreis.ecommerce.services.ProductService;
import com.uedsonreis.ecommerce.services.UserService;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	@Transactional
	private static CommandLineRunner firstUsers(UserRepository userRepository, UserService userService) {
		return (args) -> {
			Collection<User> users = userRepository.findAll();
			
			if (users == null || users.isEmpty()) {
				User admin = new User();
				admin.setAdmin(true);
				admin.setLogin("admin");
				admin.setPassword("admin");
				
				System.out.println("Admin User: "+ admin);
				userService.save(admin);
			}
		};
	}
	
	@Bean
	@Transactional
	private static CommandLineRunner firstProducts(ProductService productService, FactoryRepository factoryRepository) {
		return (args) -> {
			Collection<Factory> factories = factoryRepository.findAll();
			
			if (factories == null || factories.isEmpty()) {
				Factory apple = new Factory();
				apple.setName("Apple");
				
				Factory samsung = new Factory();
				samsung.setName("Samsung");
				
				Collection<Product> products = productService.getProducts();
				
				if (products == null || products.isEmpty()) {
					
					Product iPhone8 = new Product();
					iPhone8.setName("iPhone 8");
					iPhone8.setAmount(5);
					iPhone8.setPrice(3999.0);
					iPhone8.setFactory(apple);
					
					Product iPhoneX = new Product();
					iPhoneX.setName("iPhone X");
					iPhoneX.setAmount(10);
					iPhoneX.setPrice(4999.0);
					iPhoneX.setFactory(apple);
					
					Product galaxyS9 = new Product();
					galaxyS9.setName("Galaxy S9");
					galaxyS9.setAmount(7);
					galaxyS9.setPrice(2999.0);
					galaxyS9.setFactory(samsung);
					
					Product galaxyS10 = new Product();
					galaxyS10.setName("Galaxy S10");
					galaxyS10.setAmount(8);
					galaxyS10.setPrice(3999.0);
					galaxyS10.setFactory(samsung);
					
					try {
						productService.save(iPhone8);
						productService.save(iPhoneX);
						productService.save(galaxyS9);
						productService.save(galaxyS10);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
	} 

}