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
 * Created by user on 21/11/2017.
 */

public class OtherUserAdapter extends BaseAdapter {
        private final Context context;
        private final ArrayList<User> otherUser;
        LayoutInflater inflater;
        public OtherUserAdapter(Context context, ArrayList<User> otherUser){
            this.context=context;
            this.otherUser=otherUser;
        }
        @Override
        public int getCount(){
            return otherUser.size();
        }
        @Override
        public Object getItem(int position){
            return otherUser.get(position);
        }
        @Override
        public long getItemId(int position){
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent){
            if (inflater==null){
                inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            }
            if (convertView==null){
                convertView=inflater.inflate(R.layout.otheruser_template,parent,false);
            }
            TextView userName= (TextView) convertView.findViewById(R.id.other_username);
            TextView userScore= convertView.findViewById(R.id.other_userScore);
            ImageView userIcon=(ImageView)convertView.findViewById(R.id.other_usericon) ;
            User current_otherUser= otherUser.get(position);
            userName.setText(current_otherUser.getFirstName());
            userScore.setText("Points: "+ String.valueOf(current_otherUser.getScore()));
            userIcon.setImageResource(current_otherUser.getIcon());
            return convertView;


        }


    }
