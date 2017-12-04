package com.example.mychores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 25/11/2017.
 */

public class IconAdapter extends BaseAdapter {
    private Context mContext;
    private int[] iconList;
    LayoutInflater inflater;
    View view;

    public IconAdapter(Context c,int[] iconList) {
        mContext = c;
        this.iconList= iconList;
    }
    @Override
    public int getCount()
    {
        return iconList.length;
    }
    @Override
    public Object getItem(int position)
    {
        return iconList[position];
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
            convertView=inflater.inflate(R.layout.icondialogytemplate,null);
        }
        ImageView iconImage=  convertView.findViewById(R.id.iconimage);
        iconImage.setImageResource(iconList[position]);
        return convertView;
    }

}


