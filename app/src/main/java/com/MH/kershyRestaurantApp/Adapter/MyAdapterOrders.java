package com.MH.kershyRestaurantApp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.MH.kershyRestaurantApp.R;

import java.util.ArrayList;

import com.MH.kershyRestaurantApp.Model.User;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAdapterOrders extends RecyclerView.Adapter<MyAdapterOrders.ViewHolder> {
    private ArrayList<User> modelCartArrayList = new ArrayList<>();
    Context context;

    public MyAdapterOrders(ArrayList<User> modelArrayList, Context context) {
        this.modelCartArrayList = modelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_orders, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        User user = modelCartArrayList.get(position);
        holder.textViewNameOfItem.setText(user.getName());
        holder.textViewNumberOfItem.setText(user.getNumItem());
        holder.textViewTotalPriceCart.setText(user.getPrice());
    }

    @Override
    public int getItemCount() {
        return modelCartArrayList.size();
    }

    public void setList(ArrayList<User> models) {
        this.modelCartArrayList = models;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.textView_total_price_cart)
        TextView textViewTotalPriceCart;
        @BindView(R.id.textView_name_of_item)
        TextView textViewNameOfItem;
        @BindView(R.id.textView_number_of_item)
        TextView textViewNumberOfItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }

}
