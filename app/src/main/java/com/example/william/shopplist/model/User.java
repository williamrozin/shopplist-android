package com.example.william.shopplist.model;

import java.io.Serializable;
public class User implements Serializable{
	private long id;
	private String email;
	private String name;
	private String password;

	public String getEmail() {
		return this.email;
	}

	public String getName() {
		return this.name;
	}

	public long getId() {
		return this.id;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
