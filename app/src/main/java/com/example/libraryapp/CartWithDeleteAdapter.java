package com.example.libraryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CartWithDeleteAdapter extends RecyclerView.Adapter<CartWithDeleteAdapter.ViewHolder> {
    private List<CartModel> dataList;
    public CartWithDeleteAdapter(List<CartModel> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public CartWithDeleteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_cart_layout, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CartWithDeleteAdapter.ViewHolder holder, int position) {
        CartModel data = dataList.get(position);
        holder.bind(data);
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView quantityTextView;
        private TextView totalTextView;
        private TextView rateTextView;
        private Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            totalTextView = itemView.findViewById(R.id.totalTextView);
            rateTextView = itemView.findViewById(R.id.rateTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
        public void bind(CartModel data) {
            nameTextView.setText(data.getName());
            quantityTextView.setText("Qty: " +String.valueOf(data.getQty()));
            totalTextView.setText("Total: "+String.valueOf(data.getTotal()));
            rateTextView.setText("Rate: "+data.getRate());

            deleteButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    CartModel deleteData = dataList.get(position);
                    dataList.remove(position);
                    notifyItemRemoved(position);
                    String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference().child("cart").child(uid);
                    mDatabase.child(deleteData.getId()).removeValue();
                }
            });
        }
    }
}
