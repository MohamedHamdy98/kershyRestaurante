package com.MH.kershyRestaurantApp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.MH.kershyRestaurantApp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import com.MH.kershyRestaurantApp.Model.User;
import com.MH.kershyRestaurantApp.View.ui.Orders.UserDetailsActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAdapterUser extends RecyclerView.Adapter<MyAdapterUser.ViewHolder> {

    int index = -1;

    private Context context;

    private ArrayList<User> modelArrayList = new ArrayList<>();

    public MyAdapterUser(ArrayList<User> modelArrayList) {
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new
                ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final User user = modelArrayList.get(position);
        holder.textViewShowUserDetails.setText(user.getUserName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = position;
                notifyDataSetChanged();
                final Intent intent = new Intent(v.getContext(), UserDetailsActivity.class);
                intent.putExtra("id", modelArrayList.get(position).getId());
                intent.putExtra("name", modelArrayList.get(position).getUserName());
                intent.putExtra("phone", modelArrayList.get(position).getPhone());
                intent.putExtra("address", modelArrayList.get(position).getAddressWrite());
                intent.putExtra("totalPrice", modelArrayList.get(position).getTotalPrice());
                intent.putExtra("timeOrder", modelArrayList.get(position).getTimeOrder());
                intent.putExtra("totalPriceToPay", modelArrayList.get(position).getTotalPriceToPay());
                v.getContext().startActivity(intent);
            }
        });
        if (index == position) {
            holder.textViewShowUserDetails.setTextColor(Color.parseColor("#00CB2C"));
        } else {
            holder.textViewShowUserDetails.setTextColor(Color.parseColor("#ffffff"));
        }
//        holder.d.setOnClickListener(v -> {
//            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//            DatabaseReference databaseReference = firebaseDatabase.getReference()
//                    .child("Cart").child(user.getId());
//            databaseReference.removeValue();
//        });
        holder.d.setOnClickListener(v -> {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference()
                    .child("branchCart").child(user.getId());
            databaseReference.removeValue();
        });
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public void setList(ArrayList<User> models) {
        this.modelArrayList = models;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textView_show_user_details)
        TextView textViewShowUserDetails;
        @BindView(R.id.d)
        ImageView d;
        @BindView(R.id.card)
        CardView card;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
