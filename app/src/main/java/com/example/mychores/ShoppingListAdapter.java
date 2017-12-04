package com.example.mychores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by user on 22/11/2017.
 */

public class ShoppingListAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<ShoppingItem> itemsList;
        LayoutInflater inflater;
        public ShoppingListAdapter(Context c,ArrayList<ShoppingItem> itemsList)
        {
            mContext = c;
            this.itemsList= itemsList;
        }

        @Override
        public int getCount()
        {
            return itemsList.size();
        }
        @Override
        public Object getItem(int position)
        {
            return itemsList.get(position);
        }
        @Override
        public long getItemId(int position)
        {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (inflater==null){
                inflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            }
            if (convertView==null){
                convertView=inflater.inflate(R.layout.shopping_list_item,parent,false);
            }
            TextView item_info= (TextView) convertView.findViewById(R.id.shoppinglist_item);
            ShoppingItem currentItem= itemsList.get(position);
            //if (currentItem.getAmount()!= 0)
            item_info.setText(currentItem.getItemName()+"     X "+currentItem.getAmount());
            //else
                //item_info.setText(currentItem.getItemName());

            return convertView;
        }

}
