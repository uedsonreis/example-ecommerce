package com.uedsonreis.ecommerce.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "email")
@ToString(of = {"name", "age", "email"})
@Entity
@Table(indexes = { @Index(columnList = "email", unique = true, name = "index_email") })
public class Customer {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	private String email;
	private String name;
	private Integer age;
	private String address;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
}