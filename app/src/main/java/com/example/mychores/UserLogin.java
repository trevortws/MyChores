package com.example.mychores;

import android.content.Intent;
import android.graphics.Color;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class UserLogin extends AppCompatActivity {
    EditText username;
    EditText password;
    Button login;
    Button newuser;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
        username = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.log_in);
        newuser = findViewById(R.id.new_user);
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null)
            startActivity(new Intent(UserLogin.this, MainActivity.class));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(username.getText().toString()) || TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(UserLogin.this, "Please fill in all required information", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(username.getText().toString(), password.getText().toString());
                }
            }
        });

        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLogin.this, CreateUser.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    final String uid = mAuth.getCurrentUser().getUid();
                    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference database_user = mDatabase.getReference("User");
                    database_user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.child(uid).getValue(User.class);
                            String name = user.getFirstName();
                            Toast.makeText(UserLogin.this, "Welcome Back " + name, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    //Toast.makeText(UserLogin.this, "Welcome Back"+name, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserLogin.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(UserLogin.this, "E-mail or Password Incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
