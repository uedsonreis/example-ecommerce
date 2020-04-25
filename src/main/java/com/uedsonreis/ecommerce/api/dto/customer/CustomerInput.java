package com.uedsonreis.ecommerce.api.dto.customer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInput {

	@NotBlank
	private String email;
	
	@NotBlank
	private String name;
	
	@NotNull
	private Integer age;
	
	private String address;
	
	@NotBlank
	private String userPassword;
}