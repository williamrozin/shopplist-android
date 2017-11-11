package com.example.william.shopplist.model;

import java.io.Serializable;
public class Category implements Serializable{
	private long id;
	private String color;
	private String description;

	public String getDescription() {
		return this.description;
	}

	public String toString(){
		return String.format("%s",this.description);
	}
}
