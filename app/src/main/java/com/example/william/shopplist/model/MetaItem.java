package com.example.william.shopplist.model;

import java.io.Serializable;
public class MetaItem implements Serializable {
	private long id;
	private long userId;
	private String description;
	private Category category;

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getDescription() {
		return this.description;
	}

	public String toString(){
		return String.format("%s",this.description);
	}

	public Category getCategory() {
		return this.category;
	}

	public long getId() {
		return this.id;
	}

	public boolean equals(MetaItem metaItem) {
		return this.getId() == metaItem.getId();
	}
}
