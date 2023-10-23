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

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Toolbar toolbar;
    private RecyclerView recyclerView;
    private AdminBookHolder adapter;
    private List<BookModel> datalist = new ArrayList<>();
    DatabaseReference df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.cartToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Book App");
        df = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.bookAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminBookHolder(datalist,df);
        recyclerView.setAdapter(adapter);

        DatabaseReference bookDB = df.child("books");
        bookDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datalist.clear();
                try{
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        BookModel data = snapshot.getValue(BookModel.class);
                        data.setBookId(snapshot.getKey());
                        datalist.add(data);
                    }
                }catch (DatabaseException err){
                    Log.e("FirebaseError","Error Parsing Data"+err.getMessage());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_admin_logout){
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        if(item.getItemId()==R.id.menu_add){
            startActivity(new Intent(MainActivity.this, AddBookActivity.class));
        }

        if(item.getItemId()==R.id.menu_orders){
            startActivity( new Intent(MainActivity.this, OrderActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    }
}