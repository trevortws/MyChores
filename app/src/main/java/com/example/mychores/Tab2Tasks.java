package com.example.mychores;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 17/11/2017.
 */

public class Tab2Tasks extends Fragment {
    FirebaseDatabase mDatabase= FirebaseDatabase.getInstance();
    DatabaseReference database_task= mDatabase.getReference("Task");
    DatabaseReference database_user= mDatabase.getReference("User");
    String userToAddPoint;
    int newPoint;
    ArrayList<Task> myTaskList= new ArrayList<Task>();
    Switch taskswitch;
    ListView taskList;
    String currentUser;
    String curruid;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addTask();
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        final String uid=mAuth.getCurrentUser().getUid();
        curruid=uid;
        database_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser=dataSnapshot.child(uid).child("firstName").getValue(String.class);
                newPoint=dataSnapshot.child(uid).child("score").getValue(int.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab2tasks, container, false);
        taskList=rootView.findViewById(R.id.taskList);
        final Button button = (Button) rootView.findViewById(R.id.add_task);
        taskswitch=rootView.findViewById(R.id.task_switch);

        taskswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            addTask();
            taskList.setLongClickable(b);
            //only allow items to be longclickable if it switch is checked too
            if (taskList.isLongClickable()) {
                taskList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View view,
                                                   int i, long l) {
                    AlertDialog.Builder confirm_finish = new AlertDialog.Builder(Tab2Tasks.this.getActivity());
                    confirm_finish.setTitle("Finished?");
                    confirm_finish.setMessage("Are you sure you finished " + myTaskList.get(i).getTaskName() + "?");
                    final int positionToRemove = i;
                    confirm_finish.setNegativeButton("NO", null);
                    confirm_finish.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        final String id = myTaskList.get(positionToRemove).getDb_ID();

                        database_user.child(curruid).child("score").setValue(newPoint+1);

                        DatabaseReference dR = database_task.child(id);
                        dR.removeValue();
                        Toast.makeText(Tab2Tasks.this.getActivity(), Integer.toString(newPoint), Toast.LENGTH_LONG).show();
                        Toast.makeText(Tab2Tasks.this.getActivity(), "Task Completed. Points Added", Toast.LENGTH_LONG).show();

                        }
                    });
                    AlertDialog finished = confirm_finish.create();
                    finished.show();
                    Button neg = finished.getButton(DialogInterface.BUTTON_NEGATIVE);
                    Button pos = finished.getButton(DialogInterface.BUTTON_POSITIVE);
                    neg.setTextColor(Color.BLACK);
                    pos.setTextColor(Color.BLACK);
                    return true;

                    }


                });
            }
            }
        });
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //start the Create Task activity
                Intent intent = new Intent(Tab2Tasks.this.getActivity(), CreateTask.class);
                startActivityForResult (intent,0);
            }
        });
        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //start activity to view detail of a specific task
                Intent intent = new Intent(Tab2Tasks.this.getActivity(), TaskDetail.class);
                String dbid=myTaskList.get(i).getDb_ID();
                intent.putExtra("Task",dbid);
                startActivityForResult (intent,0);
            }
        });



        return rootView;

    }

    private void addTask(){
        //load up the TaskList from database data and show it in the ListView
        database_task.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myTaskList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Task task= snapshot.getValue(Task.class);
                    if (!taskswitch.isChecked()){
                        myTaskList.add(task);
                    }
                    else{
                        if (task.getAssigned_person().equals(currentUser)){
                            myTaskList.add(task);
                        }
                    }
                }
                TaskCustomAdapter taskCustomAdapter=new TaskCustomAdapter(Tab2Tasks.this.getActivity(),myTaskList);
                taskList.setAdapter(taskCustomAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
