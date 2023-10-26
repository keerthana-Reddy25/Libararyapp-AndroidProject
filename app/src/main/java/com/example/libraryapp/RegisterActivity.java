package com.example.libraryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout nameInputLayout, emailInputLayout, passwordInputLayout;
    Button registerBtn;
    TextView loginText;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameInputLayout = findViewById(R.id.registerNameInput);
        emailInputLayout = findViewById(R.id.registerEmailInput);
        passwordInputLayout = findViewById(R.id.registerPasswordInput);
        registerBtn = findViewById(R.id.btnRegister);
        loginText = findViewById(R.id.loginTxt);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterUser();
            }
        });
    }

    private void RegisterUser() {
        String name= nameInputLayout.getEditText().getText().toString().trim();
        String email = emailInputLayout.getEditText().getText().toString().trim().toLowerCase();
        String password = passwordInputLayout.getEditText().getText().toString().trim();
        if (TextUtils.isEmpty(name)||TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter value of all fields", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isComplete()){
                        Map<String,Object> usermap = new HashMap<>();
                        usermap.put("name", name);
                        usermap.put("email",email);
                        usermap.put("type","user");
                        String uid = mAuth.getCurrentUser().getUid();
                        mDatabase.child(uid).setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()){
                                    Intent mainIntent= new Intent(RegisterActivity.this,LoginActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mainIntent);
                                    finish();
                                }else{
                                    Toast.makeText(RegisterActivity.this, "cannot save user data", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }else{

                        Toast.makeText(RegisterActivity.this, "Cannot Create User: "+task.isSuccessful(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    }
