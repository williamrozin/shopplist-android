package com.example.william.shopplist.model;

/**
 * Created by william on 16/11/17.
 */
public class MetaItemList {
    public MetaItem metaItem;
    public boolean isChecked;

    public MetaItem getMetaItem() {
        return this.metaItem;
    }

    public void setMetaItem(MetaItem metaItem) {
        this.metaItem = metaItem;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setChecked() {
        this.isChecked = true;
    }

    public void unsetChecked() {
        this.isChecked = false;
    }

    public void toggleChecked() {
        this.isChecked = !this.isChecked;
    }
}
