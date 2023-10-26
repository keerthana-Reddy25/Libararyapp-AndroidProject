package com.example.libraryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout emailInputLayout,passwordInputLayout;
    Button loginBtn;
    TextView registerText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInputLayout = findViewById(R.id.LoginEmailInput);
        passwordInputLayout = findViewById(R.id.LoginPasswordInput);
        loginBtn = findViewById(R.id.loginBtn);
        registerText = findViewById(R.id.registerTxt);
        mAuth = FirebaseAuth.getInstance();

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= emailInputLayout.getEditText().getText().toString().trim().toLowerCase();
                String password = passwordInputLayout.getEditText().getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Please fill Email and password", Toast.LENGTH_SHORT).show();

                }else{
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                String userId = mAuth.getCurrentUser().getUid();
                                DatabaseReference userRoleRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("type");

                                userRoleRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            String userRole = dataSnapshot.getValue(String.class);
                                            if (userRole != null && userRole.equals("admin")) {
                                                // User is an admin, redirect to MainActivity
                                                Intent adminIntent = new Intent(LoginActivity.this, MainActivity.class);
                                                adminIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(adminIntent);
                                            } else {
                                                // User is not an admin, redirect to UserMainActivity
                                                Intent userIntent = new Intent(LoginActivity.this, UserMainActivity.class);
                                                userIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(userIntent);
                                            }
                                        } else {
                                            Toast.makeText(LoginActivity.this, "User role not found", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e("LoginActivity", "Error fetching user role: " + error.getMessage());

                                    }
                                });

                               /* Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(mainIntent);
                                finish();*/


                            }else{
                                Log.d("login", "onComplete: "+task.getException());
                                Toast.makeText(LoginActivity.this, "Cannot Login"+task.getException(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }


            }
        });

    }
    }
