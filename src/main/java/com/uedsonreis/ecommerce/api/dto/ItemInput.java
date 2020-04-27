package com.uedsonreis.ecommerce.api.dto;

import javax.validation.constraints.NotNull;

import com.uedsonreis.ecommerce.entities.Product;
import com.uedsonreis.ecommerce.entities.SalesOrder;

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
public class ItemInput {

	@NotNull
	private Double price;
	
	@NotNull
	private Integer amount;
	
	private Product product;
	
	private SalesOrder salesOrder;

}