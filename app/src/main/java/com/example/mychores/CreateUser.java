package com.example.mychores;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateUser extends AppCompatActivity {
    TextView function;
    EditText fName;
    EditText lName;
    EditText username;
    EditText password;
    Button register;

    private FirebaseAuth mAuth;

    private FirebaseDatabase mDatabase= FirebaseDatabase.getInstance();
    DatabaseReference database_user= mDatabase.getReference("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        function= findViewById(R.id.createuser);
        fName = findViewById(R.id.first_name);
        lName = findViewById(R.id.last_name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(fName.getText().toString())||TextUtils.isEmpty(lName.getText().toString())||TextUtils.isEmpty(username.getText().toString())||TextUtils.isEmpty(password.getText().toString())){
                    Toast.makeText(CreateUser.this,"Please fill in all required information",Toast.LENGTH_SHORT).show();
                }
                else if(function.getText().toString()!=getString(R.string.edit_task)) {
                    signUpUser(username.getText().toString(),password.getText().toString());
                }
            }
        });
    }

    private void signUpUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            String id =mAuth.getCurrentUser().getUid();
                            User user= new User(id,fName.getText().toString(),lName.getText().toString(),0,R.drawable.monkey);
                            database_user.child(id).setValue(user);
                            Toast.makeText(CreateUser.this,"Welcome to MyChore "+user.getFirstName(),Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(CreateUser.this,UserLogin.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(CreateUser.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
