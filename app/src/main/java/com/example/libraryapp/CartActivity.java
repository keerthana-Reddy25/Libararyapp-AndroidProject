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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    Toolbar mToolbar;

    private RecyclerView recyclerView;
    FirebaseAuth mAuth;
    private Button checkoutButton, goBackButton;
    private List<CartModel> dataList = new ArrayList<>();
    DatabaseReference mDatabase;
    CartWithDeleteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mToolbar= findViewById(R.id.cartToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth= FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        String uid = mAuth.getCurrentUser().getUid();

        recyclerView = findViewById(R.id.cartAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartWithDeleteAdapter(dataList);
        recyclerView.setAdapter(adapter);
        DatabaseReference cartDatabase= mDatabase.child("cart").child(uid);
        checkoutButton = findViewById(R.id.checkoutButton);
        goBackButton = findViewById(R.id.goBackButton);
        cartDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataList.clear();
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        CartModel data = snapshot.getValue(CartModel.class);
                        data.setId(snapshot.getKey());
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

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                mDatabase.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String userName= snapshot.child("name").getValue().toString();
                        String email=snapshot.child("email").getValue().toString();
                        for(CartModel cart : dataList){
                            Map<String,Object> orderMap= new HashMap<>();
                            orderMap.put("book_name",cart.getName());
                            orderMap.put("qty",cart.getQty());
                            orderMap.put("rate",cart.getRate());
                            orderMap.put("total",cart.getTotal());
                            orderMap.put("customerName",userName);
                            orderMap.put("email",email);
                            orderMap.put("customer_id",uid);

                            mDatabase.child("orders").push().setValue(orderMap);

                        }
                        mDatabase.child("cart").child(uid).removeValue();
                        Toast.makeText(CartActivity.this, "Your order is successfully created!!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, UserMainActivity.class));
                finish();
            }
        });

    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.checkout_menu){
            String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
            mDatabase.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String userName= snapshot.child("name").getValue().toString();
                    String email=snapshot.child("email").getValue().toString();
                    for(CartModel cart : dataList){
                        Map<String,Object> orderMap= new HashMap<>();
                        orderMap.put("book_name",cart.getName());
                        orderMap.put("qty",cart.getQty());
                        orderMap.put("rate",cart.getRate());
                        orderMap.put("total",cart.getTotal());
                        orderMap.put("customerName",userName);
                        orderMap.put("email",email);
                        orderMap.put("customer_id",uid);

                        mDatabase.child("orders").push().setValue(orderMap);

                    }
                    mDatabase.child("cart").child(uid).removeValue();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        return super.onOptionsItemSelected(item);
    }*/

}