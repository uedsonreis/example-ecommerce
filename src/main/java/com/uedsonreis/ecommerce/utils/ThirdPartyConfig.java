package com.uedsonreis.ecommerce.utils;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThirdPartyConfig {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
}