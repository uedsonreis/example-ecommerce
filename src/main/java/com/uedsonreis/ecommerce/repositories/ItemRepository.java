package com.uedsonreis.ecommerce.repositories;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uedsonreis.ecommerce.entities.Item;
import com.uedsonreis.ecommerce.entities.SalesOrder;

@Repository
public interface ItemRepository extends CrudRepository<Item, Integer> {

	public Set<Item> findAllBySalesOrder(SalesOrder salesOrder);
	
}