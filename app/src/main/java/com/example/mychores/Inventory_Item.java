package com.example.mychores;

/**
 * Created by user on 1/12/2017.
 */

public class Inventory_Item {
    String dbid;
    String itemName;

    public String getDbid() {
        return dbid;
    }

    public void setDbid(String dbid) {
        this.dbid = dbid;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String name) {
        itemName = name;
    }

    public Inventory_Item(String dbid, String name) {

        this.dbid = dbid;
        this.itemName = name;
    }


    public Inventory_Item() {
    }
}
