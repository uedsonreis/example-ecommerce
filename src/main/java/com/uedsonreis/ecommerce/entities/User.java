package com.uedsonreis.ecommerce.entities;

public class User {

	private String login;
	private String password;
	private Boolean admin;
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Boolean getAdmin() {
		return admin;
	}
	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		User other = (User) obj;
		if (login == null) {
			if (other.login != null) return false;
		} else if (!login.equals(other.login)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "User [login=" + login + ", admin=" + admin + "]";
	}

}