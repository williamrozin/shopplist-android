package com.example.william.shopplist.model;

import java.io.Serializable;
public class MetaItem implements Serializable {
	private long id;
	private String description;
	private Category category;

	public String getDescription() {
		return this.description;
	}

	public String toString(){
		return String.format("%s",this.description);
	}
}
