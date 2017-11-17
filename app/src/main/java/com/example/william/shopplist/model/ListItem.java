package com.example.william.shopplist.model;

import java.io.Serializable;
import java.io.StreamCorruptedException;

public class ListItem implements Serializable{
	private long id;
	private MetaItem metaItem;
	private boolean checked;
	private float price;

	public String getDescription() {
		return this.metaItem.getDescription();
	}

	@Override
	public String toString() {
		return this.getDescription();
	}

	public void setMetaItem(MetaItem metaItem) {
		this.metaItem = metaItem;
	}
}
