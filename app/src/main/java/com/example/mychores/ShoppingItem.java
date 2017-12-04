package com.example.mychores;

/**
 * Created by user on 22/11/2017.
 */

public class ShoppingItem {
    String itemName;
    int amount;
    String db_id;

    public ShoppingItem() {
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDb_id() {
        return db_id;
    }

    public void setDb_id(String db_id) {
        this.db_id = db_id;
    }

    public ShoppingItem( String db_id, String itemName, int amount) {

        this.itemName = itemName;
        this.amount = amount;
        this.db_id=db_id;
    }
}
