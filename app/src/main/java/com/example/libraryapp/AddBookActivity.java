package com.example.libraryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class AddBookActivity extends AppCompatActivity {
    private EditText bookNameInput,bookGenreInput,bookAuthorInput,bookPriceInput;
    private ImageView bookImage;
    private Toolbar toolbar;
    private DatabaseReference df;
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Uri imageUri;
    String DownloadUrl="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        toolbar = findViewById(R.id.cartToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Book");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        df= FirebaseDatabase.getInstance().getReference();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        bookNameInput = findViewById(R.id.bookNameInput);
        bookGenreInput = findViewById(R.id.bookGenreInput);
        bookAuthorInput = findViewById(R.id.bookAuthorInput);
        bookPriceInput = findViewById(R.id.bookPriceInput);
        bookImage = findViewById(R.id.bookImageBtn);
        bookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();

            // Upload the selected image to Firebase Storage
            StorageReference imageRef = storageRef.child("images");
            UploadTask uploadTask = imageRef.putFile(imageUri);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        // Image uploaded successfully
                        // You can also get the download URL of the uploaded image
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                DownloadUrl=downloadUri.toString();
                                // Set the downloaded image URL to the ImageView
                                Picasso.get().load(downloadUri).into(bookImage);
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.menu_done){
            AddBookToDatabase();


        }
        return super.onOptionsItemSelected(item);
    }

    private void AddBookToDatabase(){
        String bookName = bookNameInput.getText().toString().trim();
        String bookGenre = bookGenreInput.getText().toString().trim();
        String bookAuthor = bookAuthorInput.getText().toString().trim();
        String bookPrice = bookPriceInput.getText().toString().trim();

        if(TextUtils.isEmpty(bookName) || TextUtils.isEmpty(bookGenre) || TextUtils.isEmpty(bookAuthor) || TextUtils.isEmpty(bookPrice)){
            Toast.makeText(AddBookActivity.this, "All the fields are required", Toast.LENGTH_SHORT).show();
        }else{
            Map<String,Object> bookMap = new HashMap<>();
            bookMap.put("bookName",bookName);
            bookMap.put("bookGenre",bookGenre);
            bookMap.put("bookAuthor",bookAuthor);
            bookMap.put("bookPrice",bookPrice);
            bookMap.put("image_url",DownloadUrl);

            df.child("books").push().setValue(bookMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isComplete()){
                        Toast.makeText(AddBookActivity.this, "Book is Successfully added!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddBookActivity.this, MainActivity.class));
                        finish();
                    }else{
                        Toast.makeText(AddBookActivity.this, "Error occurred while adding a book. Try Again!", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

    }


}
