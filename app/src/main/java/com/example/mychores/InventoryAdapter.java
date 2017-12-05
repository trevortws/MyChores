package com.example.mychores;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by user on 1/12/2017.
 */

public class InventoryAdapter extends ArrayAdapter<Inventory_Item> {
    private Context mcontext;
    List<Inventory_Item> inventory_items;
    LayoutInflater inflater;

    public InventoryAdapter(Context context, List<Inventory_Item> item) {
        super(context, R.layout.inventory_item, item);
        this.mcontext = context;
        this.inventory_items = item;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) mcontext.getSystemService(mcontext.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.inventory_item, parent, false);

            TextView textViewName = (TextView) convertView.findViewById(R.id.invitemname);

            Inventory_Item item = inventory_items.get(position);
            textViewName.setText(item.getItemName());
        }
        return convertView;
    }

}
