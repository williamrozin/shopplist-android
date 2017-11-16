package com.example.william.shopplist.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ShoppingList implements Serializable{
	private long id;
	private String description;
	private boolean completed;
	private long userId;
	private Date date;
	private List<ListItem> items;

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
}
