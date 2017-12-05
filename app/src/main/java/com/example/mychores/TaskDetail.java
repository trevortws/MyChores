package com.example.mychores;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TaskDetail extends AppCompatActivity {
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference database_task = mDatabase.getReference("Task");
    TextView name;
    TextView describiton;
    TextView avgtime;
    TextView creator;
    TextView duedate;
    ImageView icon;
    TextView status;
    TextView assignedPerson;
    Task task;
    GridView itemlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskdetail);
        name = findViewById(R.id.taskdetail_name);
        describiton = findViewById(R.id.taskdetail_notes);
        avgtime = findViewById(R.id.avg_time);
        creator = findViewById(R.id.creator_name);
        duedate = findViewById(R.id.due_date);
        icon = findViewById(R.id.taskdetail_icon);
        status = findViewById(R.id.taskdetail_status);
        itemlist = findViewById(R.id.taskitemlist);
        assignedPerson = findViewById(R.id.assigned_person);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            final String dbid = bundle.getString("Task");
            database_task.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    task = dataSnapshot.child(dbid).getValue(Task.class);
                    name.setText(task.getTaskName());
                    describiton.setText(task.getTaskDescribtion());
                    creator.setText("Creator: " + task.getCreator());
                    avgtime.setText(task.getAvgTime());
                    duedate.setText(task.getDue_date());
                    icon.setImageResource(task.getTaskIcon());
                    assignedPerson.setText("Assigned User: " + task.getAssigned_person());
                    status.setText(task.getStatus());
                    if (task.getStatus().equals("Status: Urgent")) {
                        status.setTextColor(Color.parseColor("#f44268"));
                    }
                    InventoryAdapter adapter = new InventoryAdapter(TaskDetail.this, task.getItems_needed());

                    itemlist.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }


        ImageButton editTask = findViewById(R.id.edit_task);
        editTask.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String dbid = task.getDb_ID();
                Intent intent = new Intent(TaskDetail.this, CreateTask.class);
                intent.putExtra("Task", dbid);
                startActivity(intent);
            }
        });
        ImageButton delete = findViewById(R.id.delete_task);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmDialog = new AlertDialog.Builder(TaskDetail.this);
                View dialog = getLayoutInflater().inflate(R.layout.confirm_delete, null);
                confirmDialog.setView(dialog);
                final AlertDialog mDialog = confirmDialog.create();
                mDialog.show();
                Button cancel = dialog.findViewById(R.id.delete_cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialog.hide();

                    }
                });
                Button confirm = dialog.findViewById(R.id.delete_confirm);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteData(task.getDb_ID());
                    }
                });


            }


        });


    }

    public void deleteData(String dbid) {
        database_task.child(dbid).removeValue();
        try {
            Intent intent = new Intent(TaskDetail.this, MainActivity.class);
            startActivity(intent);
        } catch (NullPointerException e) {
            Toast.makeText(TaskDetail.this, "idk", Toast.LENGTH_LONG).show();
        }
    }


}
