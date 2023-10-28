package com.example.libraryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserBookAdapter extends RecyclerView.Adapter<UserBookAdapter.ViewHolder>{
    private List<BookModel> dataList;

    public UserBookAdapter(List<BookModel> dataList) {
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public UserBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user_book_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserBookAdapter.ViewHolder holder, int position) {
        BookModel data = dataList.get(position);
        holder.bind(data);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView descriptionTextView;
        private TextView rateTextView;
        private TextView quantityTextView;
        private Button addToCartButton;
        private Button incrementButton;
        private Button decrementButton;
        private ImageView singleLayoutImage;
        private int quantity = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            rateTextView = itemView.findViewById(R.id.rateTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
            incrementButton = itemView.findViewById(R.id.incrementButton);
            decrementButton = itemView.findViewById(R.id.decrementButton);

            singleLayoutImage = itemView.findViewById(R.id.singleUserFoodImage);
        }

        public void bind(BookModel data) {
            nameTextView.setText(data.getBookName());
            descriptionTextView.setText(data.getBookGenre());
            rateTextView.setText("Price: "+data.getBookPrice());

            quantityTextView.setText(String.valueOf(quantity));
            //Picasso.get().load(data.getImage_url()).into(singleLayoutImage);
            if (data.getImage_url() != null && !data.getImage_url().isEmpty()) {
                Picasso.get().load(data.getImage_url()).into(singleLayoutImage);
            } else {

                Picasso.get().load(R.drawable.default_image)
                        .into(singleLayoutImage);
            }

            incrementButton.setOnClickListener(view -> {
                quantity++;
                quantityTextView.setText(String.valueOf(quantity));
            });

            decrementButton.setOnClickListener(view -> {
                if (quantity > 0) {
                    quantity--;
                    quantityTextView.setText(String.valueOf(quantity));
                }
            });

            addToCartButton.setOnClickListener(view -> {
                // Handle "Add to Cart" button click with the selected quantity
                Map<String, Object> cartMap = new HashMap<>();
                cartMap.put("book_id",data.getBookId());
                cartMap.put("name",data.getBookName());
                cartMap.put("rate", data.getBookPrice());
                cartMap.put("image",data.getImage_url());
                cartMap.put("qty", quantity);
                Double rate = Double.parseDouble(data.getBookPrice());
                Double total = rate*quantity;
                cartMap.put("total",total);
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("cart")
                        .child(uid).push().setValue(cartMap);
                quantity=0;
                quantityTextView.setText(String.valueOf(quantity));

            });
        }
    }


}
