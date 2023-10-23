package com.example.libraryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{
    private List<Order> orderList;
    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }
    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_order_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order);

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView customerNameTextView;
        private TextView customerEmailTextView;
        private TextView bookNameTextView;
        private TextView quantityTextView;
        private TextView rateTextView;
        private TextView totalTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            customerNameTextView = itemView.findViewById(R.id.customerNameTextView);
            customerEmailTextView = itemView.findViewById(R.id.customerEmailTextView);
            bookNameTextView = itemView.findViewById(R.id.bookNameTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            rateTextView = itemView.findViewById(R.id.rateTextView);
            totalTextView = itemView.findViewById(R.id.totalTextView);
        }

        public void bind(Order order) {
            customerNameTextView.setText("Order By: "+order.getCustomerName());
            customerEmailTextView.setText(order.getEmail());
            bookNameTextView.setText(order.getBookName());
            quantityTextView.setText("Qty: "+ String.valueOf(order.getQty()));
            rateTextView.setText("Rate: "+order.getRate());
            totalTextView.setText("Total: "+order.getTotal());
        }
    }
}
