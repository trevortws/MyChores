package com.example.mychores;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.BoringLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CreateTask extends AppCompatActivity {
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener DateListner;
    TextView function;
    EditText name;
    EditText describtion;
    EditText avgtime;
    ToggleButton status;
    Button assigned_person;
    Button submit;
    ArrayList<String> userList = new ArrayList<>();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference database_task = mDatabase.getReference("Task");
    DatabaseReference database_inventory = mDatabase.getReference("Inventory");
    DatabaseReference database_user = mDatabase.getReference("User");
    Task task;
    List<Inventory_Item> invItems = new ArrayList<>();
    List<Inventory_Item> item_add_todb = new ArrayList<>();
    GridView itemlist;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String currentUserName;
    String uid;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        mDisplayDate = (TextView) findViewById(R.id.new_duedate);
        function = findViewById(R.id.createtask);
        name = findViewById(R.id.new_name);
        describtion = findViewById(R.id.new_describtion);
        avgtime = findViewById(R.id.new_avg_time);
        status = findViewById(R.id.new_status);
        assigned_person = findViewById(R.id.assign_user);
        submit = findViewById(R.id.new_submit);
        itemlist = findViewById(R.id.itemlist);
        addUser();
        Bundle bundle = getIntent().getExtras();
        uid = mAuth.getCurrentUser().getUid();
        database_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(uid).getValue(User.class);
                String name = user.getFirstName();
                currentUserName = name;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (bundle != null) {
            final String dbid = bundle.getString("Task");
            database_task.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    task = dataSnapshot.child(dbid).getValue(Task.class);
                    name.setText(task.getTaskName());
                    describtion.setText(task.getTaskDescribtion());
                    mDisplayDate.setText(task.getDue_date());
                    if (task.getStatus().equals("Status: Urgent")) {
                        status.setChecked(true);
                    }
                    assigned_person.setText("Assigned User: " + task.getAssigned_person());
                    assigned_person.setBackgroundColor(Color.argb(75, 144, 195, 212));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            function.setText(getString(R.string.edit_task));


        }


        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateTask.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        DateListner,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        DateListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };
        final Button assign_user = findViewById(R.id.assign_user);
        assign_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateTask.this);
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.assign_user, null);
                alertDialog.setView(convertView);
                alertDialog.setTitle("Choose a user");
                ListView lv = convertView.findViewById(R.id.assign_user_dialog_list);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateTask.this, android.R.layout.simple_list_item_1, userList);
                lv.setAdapter(adapter);
                final AlertDialog mDialog = alertDialog.create();

                mDialog.show();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                        String assigned_user = userList.get(position);
                        assign_user.setText("Assigned User: " + assigned_user);
                        assign_user.setBackgroundColor(Color.argb(75, 144, 195, 212));
                        mDialog.hide();
                    }
                });

            }

        });
        Button edit_submit = findViewById(R.id.new_submit);
        edit_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id;
                if (TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(assigned_person.getText().toString())) {
                    Toast.makeText(CreateTask.this, "Please enter task name and assign person", Toast.LENGTH_SHORT).show();
                } else if (!function.getText().toString().equals(getString(R.string.edit_task))) {
                    id = database_task.push().getKey();
                    dbsubmit(id);
                } else if (function.getText().toString().equals(getString(R.string.edit_task))) {
                    id = task.getDb_ID();
                    dbsubmit(id);
                }
            }
        });
        database_inventory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                invItems.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Inventory_Item item = snapshot.getValue(Inventory_Item.class);
                    invItems.add(item);

                }
                CreateTaskItemAdapter adapter = new CreateTaskItemAdapter(CreateTask.this, invItems);

                itemlist.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));


    }


    private void addUser() {
        database_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User db_user = snapshot.getValue(User.class);
                    String username = db_user.getFirstName();
                    userList.add(username);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void dbsubmit(String id) {
        String desctibtionText = describtion.getText().toString();
        String time = avgtime.getText().toString();
        String due_date = mDisplayDate.getText().toString();
        String assuser = assigned_person.getText().toString();
        assuser = assuser.replace("Assigned User: ", "");
        String assignusericon;

        if (TextUtils.isEmpty(desctibtionText)) {
            desctibtionText = "null";
        }
        if (TextUtils.isEmpty(time)) {
            time = "#";
        }
        if (TextUtils.isEmpty(due_date) || due_date.equals(" Pick A Due Date")) {
            due_date = "Not Specify";
        }
        if (item_add_todb.size() == 0) {
            item_add_todb.add(new Inventory_Item("null", "null"));
        }
        String[] timesplit = time.split(":");
        if (timesplit.length == 2) {
            due_date = timesplit[0] + "hr " + timesplit[1] + "mins";

        } else {
            due_date = timesplit[0] + "mins";

        }

        Task task = new Task(id, name.getText().toString(), desctibtionText, R.drawable.mopping, due_date, currentUserName, mDisplayDate.getText().toString(), status.getText().toString(), assuser, item_add_todb);
        database_task.child(id).setValue(task);
        Intent intent = new Intent(CreateTask.this, MainActivity.class);
        startActivity(intent);

    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String name = intent.getStringExtra("name");
            final String dbid = intent.getStringExtra("dbid");
            final Boolean status = intent.getBooleanExtra("status", false);
            Inventory_Item adding_item = new Inventory_Item(dbid, name);
            if (status) {
                item_add_todb.add(adding_item);
            } else {
                item_add_todb.remove(adding_item);
            }


        }
    };


}


