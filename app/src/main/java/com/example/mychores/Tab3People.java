package com.example.mychores;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by user on 17/11/2017.
 */

public class Tab3People extends Fragment {
    ArrayList<User> UserList = new ArrayList<User>();
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference database_user = mDatabase.getReference("User");
    TextView overdue_task;
    TextView currUserName;
    TextView score;
    ImageView currUserIcon;
    ListView userListView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final String currUid = mAuth.getCurrentUser().getUid();
    int[] IconList = {
            R.drawable.bunny,
            R.drawable.cat,
            R.drawable.cow,
            R.drawable.dog,
            R.drawable.monkey,
            R.drawable.moose,
            R.drawable.owl,
            R.drawable.penguin,
            R.drawable.polarbear,
            R.drawable.seal,
            R.drawable.wolf,
            R.drawable.pig
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addUser();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab3people, container, false);
        userListView = rootView.findViewById(R.id.other_userlist);
        OtherUserAdapter otherUserAdapter = new OtherUserAdapter(this.getActivity(), UserList);
        userListView.setAdapter(otherUserAdapter);

        final ImageView userIcon = rootView.findViewById(R.id.userIcon);
        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Tab3People.this.getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.changeicon, null);
                alertDialog.setView(convertView);
                alertDialog.setTitle("Choose a Icon");
                GridView icontable = convertView.findViewById(R.id.icon_table);
                final IconAdapter iconAdapter = new IconAdapter(Tab3People.this.getActivity(), IconList);
                icontable.setAdapter(iconAdapter);
                final AlertDialog mDialog = alertDialog.create();
                mDialog.show();
                icontable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        database_user.child(currUid).child("icon").setValue(IconList[i]);
                        mDialog.hide();
                    }
                });

            }
        });
        overdue_task = rootView.findViewById(R.id.overDueTask);
        overdue_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Tab3People.this.getActivity(), "Feature Coming Soon", Toast.LENGTH_LONG).show();
            }
        });
        currUserName = rootView.findViewById(R.id.UserName);
        currUserIcon = rootView.findViewById(R.id.userIcon);
        score = rootView.findViewById(R.id.userPoints);
        database_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(currUid).getValue(User.class);
                String name = user.getFirstName() + " " + user.getLastName();
                int icon = user.getIcon();
                currUserIcon.setImageResource(icon);
                currUserName.setText(name);
                score.setText(Integer.toString(user.getScore()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return rootView;


    }

    public void addUser() {
        database_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    UserList.add(user);
                }
                OtherUserAdapter otherUserAdapter = new OtherUserAdapter(Tab3People.this.getActivity(), UserList);
                userListView.setAdapter(otherUserAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
