package com.example.mychores;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Inventory extends AppCompatActivity {
    List<Inventory_Item> InvItems = new ArrayList<>();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference database_inventory = mDatabase.getReference("Inventory");
    GridView inventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        inventory = (GridView) findViewById(R.id.inventory);
        Button add_button = findViewById(R.id.inventory_addbutton);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText item_name = findViewById(R.id.inventoery_add_name);
                if (TextUtils.isEmpty(item_name.getText().toString())) {
                    Toast.makeText(Inventory.this, "Please enter the item's name", Toast.LENGTH_SHORT).show();
                } else {
                    String dbid = database_inventory.push().getKey();
                    Inventory_Item item = new Inventory_Item(dbid, item_name.getText().toString());
                    database_inventory.child(dbid).setValue(item);
                }
            }
        });
        inventory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Inventory_Item item = InvItems.get(i);
                database_inventory.child(item.getDbid()).removeValue();
                return false;

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        database_inventory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                InvItems.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Inventory_Item item = snapshot.getValue(Inventory_Item.class);
                    InvItems.add(item);

                }
                InventoryAdapter adapter = new InventoryAdapter(Inventory.this, InvItems);

                inventory.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
