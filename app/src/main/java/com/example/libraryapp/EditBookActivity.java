package com.example.libraryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditBookActivity extends AppCompatActivity {
    private EditText nameInputLayout, genreInputLayout, authorInputLayout, priceInputLayout;
    private Button updateButton;
    private BookModel bookToEdit;
    private DatabaseReference df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);
        nameInputLayout = findViewById(R.id.editBookNameInput);
        genreInputLayout = findViewById(R.id.editGenreInput);
        authorInputLayout = findViewById(R.id.editAuthorInput);
        priceInputLayout = findViewById(R.id.editPriceInput);
        updateButton = findViewById(R.id.updateBookButton);
        df = FirebaseDatabase.getInstance().getReference();
        bookToEdit = (BookModel) getIntent().getSerializableExtra("bookToEdit");
        nameInputLayout.setText(bookToEdit.getBookName());
        genreInputLayout.setText(bookToEdit.getBookGenre());
        authorInputLayout.setText(bookToEdit.getBookAuthor());
        priceInputLayout.setText(bookToEdit.getBookPrice().toString());

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String updatedName = nameInputLayout.getText().toString();
                String updatedGenre = genreInputLayout.getText().toString();
                String updatedAuthor = authorInputLayout.getText().toString();
                String updatedPrice =priceInputLayout.getText().toString();
                df.child("books").child(bookToEdit.getBookId()).child("bookName").setValue(updatedName);
                df.child("books").child(bookToEdit.getBookId()).child("bookGenre").setValue(updatedGenre);
                df.child("books").child(bookToEdit.getBookId()).child("bookAuthor").setValue(updatedAuthor);
                df.child("books").child(bookToEdit.getBookId()).child("bookPrice").setValue(updatedPrice);
                finish();
            }
        });
    }
}
