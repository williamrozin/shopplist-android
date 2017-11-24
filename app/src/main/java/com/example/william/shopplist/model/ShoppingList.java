package com.example.william.shopplist.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShoppingList implements Serializable{
	private long id;
	private String description;
	private boolean completed;
	private long userId;
	private Date date;
	private List<ListItem> items = new ArrayList<>();

    public String getDescription() {
        return this.description;
    }

    public String getDate() {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date());
    }

    public List<ListItem> getItems() {
        return this.items;
    }

    public String toString(){
        return String.format("%s",this.description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addListItem(ListItem listItem) {
        this.items.add(listItem);
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getId() {
        return this.id;
    }

    public long getUserId() {
        return this.userId;
    }
}
