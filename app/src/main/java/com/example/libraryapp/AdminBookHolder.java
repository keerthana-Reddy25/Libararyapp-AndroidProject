package com.example.libraryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminBookHolder extends RecyclerView.Adapter<AdminBookHolder.ViewHolder> {
    private List<BookModel> datalist;
    private DatabaseReference df;

    public AdminBookHolder(List<BookModel> dataList, DatabaseReference df) {
        this.datalist = dataList;
        this.df = df;
    }

    @NonNull
    @Override
    public AdminBookHolder.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminBookHolder.ViewHolder holder, int position) {
        BookModel data = datalist.get(position);
        holder.bind(data);

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView bookName,genre,author,price;
        private ImageView singleLayoutImage;
        private Button updateButton;
        private Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.bookNameTextView);
            genre = itemView.findViewById(R.id.genreTextView);
            author = itemView.findViewById(R.id.authorTextView);
            price = itemView.findViewById(R.id.priceTextView);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            singleLayoutImage = itemView.findViewById(R.id.singleLayoutBookImage);
        }

        public void bind(BookModel data) {
            bookName.setText(data.getBookName());
            genre.setText(data.getGenre());
            author.setText(data.getBookAuthor());
            price.setText("Price: "+data.getBookPrice().toString());

            updateButton.setOnClickListener(view -> {
                // Handle update button click, e.g., navigate to an update activity
            });
            Picasso.get().load(data.getImage_url()).into(singleLayoutImage);

            deleteButton.setOnClickListener(view -> {
                // Handle delete button click
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    BookModel itemToDelete = datalist.get(position);
                    String itemKey = itemToDelete.getBookId();

                    // Remove the item from the list and the database
                    datalist.remove(position);
                    notifyItemRemoved(position);

                    // Delete the item from the database
                    df.child("books").child(itemKey).removeValue();
                }
            });
        }
    }
}
