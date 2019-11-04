package com.uedsonreis.ecommerce.entities;

public class Customer {

	private String email;
	private String name;
	private Integer age;
	private String address;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		Customer other = (Customer) obj;
		if (email == null) {
			if (other.email != null) return false;
		} else if (!email.equals(other.email)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "Customer [email=" + email + ", name=" + name + ", age=" + age + "]";
	}
	
}