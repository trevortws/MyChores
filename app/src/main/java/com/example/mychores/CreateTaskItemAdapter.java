package com.example.mychores;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

/**
 * Created by user on 2/12/2017.
 */

public class CreateTaskItemAdapter extends ArrayAdapter<Inventory_Item> {
    private Context mcontext;
    List<Inventory_Item> inventory_items;
    LayoutInflater inflater;
    CheckBox itemname;

    public CreateTaskItemAdapter(Context context, List<Inventory_Item> item) {
        super(context, R.layout.inventory_item, item);
        this.mcontext = context;
        this.inventory_items = item;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) mcontext.getSystemService(mcontext.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.creat_task_item, parent, false);

            Inventory_Item item = inventory_items.get(position);
            itemname = convertView.findViewById(R.id.itemcheck);
            itemname.setText(item.getItemName());
            itemname.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    String name = inventory_items.get(position).getItemName();
                    String dbid = inventory_items.get(position).getDbid();
                    boolean status = b;

                    Intent intent = new Intent("custom-message");
                    intent.putExtra("status", status);
                    intent.putExtra("name", name);
                    intent.putExtra("dbid", dbid);
                    LocalBroadcastManager.getInstance(mcontext).sendBroadcast(intent);
                }
            });
        }
        return convertView;
    }


}