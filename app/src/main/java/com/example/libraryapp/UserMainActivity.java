package com.example.libraryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserMainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Toolbar mToolbar;
    private Button cartButton, logoutButton;

    private RecyclerView recyclerView;
    private UserBookAdapter adapter;
    private List<BookModel> dataList = new ArrayList<>();
    DatabaseReference mDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        mAuth= FirebaseAuth.getInstance();
        mToolbar= findViewById(R.id.cartToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Book App");
        mDatabase= FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById(R.id.userBookAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserBookAdapter(dataList);
        recyclerView.setAdapter(adapter);
        cartButton = findViewById(R.id.cartButton);
        logoutButton = findViewById(R.id.logoutButton);


        DatabaseReference bookDatabase =mDatabase.child("books");
        bookDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataList.clear();
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        BookModel data = snapshot.getValue(BookModel.class);
                        data.setBookId(snapshot.getKey());
                        dataList.add(data);
                    }
                } catch (DatabaseException e) {
                    Log.e("FirebaseError", "Error parsing data: " + e.getMessage());
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserMainActivity.this, CartActivity.class));

            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserMainActivity.this, LoginActivity.class));

            }
        });

    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.user_cart){
            startActivity(new Intent(UserMainActivity.this, CartActivity.class));

        }
        if(item.getItemId()==R.id.menu_user_logout){
            mAuth.signOut();
            Intent loginIntent = new Intent(UserMainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }*/


}