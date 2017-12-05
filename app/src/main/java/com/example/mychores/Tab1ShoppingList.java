package com.example.mychores;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by user on 17/11/2017.
 */

public class Tab1ShoppingList extends Fragment {
    ArrayList<ShoppingItem> myShoppingList = new ArrayList<ShoppingItem>();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference database_shopping = mDatabase.getReference("shoppinglist");
    ListView shopping_list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addShoppinglist();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab1shoppinglist, container, false);
        shopping_list = rootView.findViewById(R.id.shoppinglist);
        final ShoppingListAdapter shoppingListAdapter = new ShoppingListAdapter(this.getActivity(), myShoppingList);
        shopping_list.setAdapter(shoppingListAdapter);
        final EditText newitemName = rootView.findViewById(R.id.add_item_name);
        final EditText newitemAmount = rootView.findViewById(R.id.add_item_number);
        final Button itemsubmit = rootView.findViewById(R.id.add_item_submit);

        itemsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(newitemName.getText().toString()) || TextUtils.isEmpty(newitemAmount.getText().toString())) {
                    Toast.makeText(getActivity(), "Please Enter both name and amount", Toast.LENGTH_SHORT).show();
                } else {
                    String id = database_shopping.push().getKey();
                    ShoppingItem item = new ShoppingItem(id, newitemName.getText().toString(), Integer.parseInt(newitemAmount.getText().toString()));
                    database_shopping.child(id).setValue(item);
                }
            }

        });

        shopping_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String itemname = myShoppingList.get(i).getItemName();
                String itemamount = String.valueOf(myShoppingList.get(i).getAmount());
                final String dbid = myShoppingList.get(i).getDb_id();
                AlertDialog.Builder itemchange = new AlertDialog.Builder(Tab1ShoppingList.this.getActivity());
                itemchange.setTitle("Item Changes ");
                View dialog = getLayoutInflater().inflate(R.layout.shopping_item_changes, null);
                itemchange.setView(dialog);
                final AlertDialog mDialog = itemchange.create();
                mDialog.show();
                final EditText name = dialog.findViewById(R.id.itemchangename);
                final EditText amount = dialog.findViewById(R.id.itemchangeamount);
                name.setText(itemname);
                amount.setText(itemamount);
                Button submit = dialog.findViewById(R.id.itemchangeconfirm);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShoppingItem item = new ShoppingItem(dbid, name.getText().toString(), Integer.parseInt(amount.getText().toString()));
                        database_shopping.child(dbid).setValue(item);
                        mDialog.hide();

                    }
                });


            }
        });
        shopping_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view,
                                           int i, long l) {
                AlertDialog.Builder confirm_finish = new AlertDialog.Builder(Tab1ShoppingList.this.getActivity());
                confirm_finish.setTitle("Remove Item");
                confirm_finish.setMessage("Delete " + myShoppingList.get(i).getItemName() + "?");
                final int positionToRemove = i;
                confirm_finish.setNegativeButton("NO", null);
                confirm_finish.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String id = myShoppingList.get(positionToRemove).getDb_id();
                        DatabaseReference dR = database_shopping.child(id);
                        dR.removeValue();
                        Toast.makeText(Tab1ShoppingList.this.getActivity(), "Item Delected", Toast.LENGTH_LONG).show();
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

        return rootView;

    }

    public void addShoppinglist() {
        database_shopping.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myShoppingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ShoppingItem item = snapshot.getValue(ShoppingItem.class);
                    myShoppingList.add(item);

                }
                ShoppingListAdapter shoppingListAdapter = new ShoppingListAdapter(Tab1ShoppingList.this.getActivity(), myShoppingList);
                shopping_list.setAdapter(shoppingListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
