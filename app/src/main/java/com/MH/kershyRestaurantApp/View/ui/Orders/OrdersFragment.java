package com.MH.kershyRestaurantApp.View.ui.Orders;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.MH.kershyRestaurantApp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.MH.kershyRestaurantApp.Adapter.MyAdapterUser;
import com.MH.kershyRestaurantApp.Model.User;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OrdersFragment extends Fragment {
    @BindView(R.id.recyclerView_orders)
    RecyclerView recyclerViewOrders;
    private MyAdapterUser myAdapterUser;
    private ArrayList<User> userArrayList;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference("branchCart");
    @BindView(R.id.prgressBar)
    ProgressBar prgressBar;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_orders, container, false);
        ButterKnife.bind(this,root);
        start_RecyclerView();
        // To Stop button back...
        root.setFocusableInTouchMode(true);
        root.requestFocus();
        root.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
            }
            return false;
        });
        return root;
    }

    private void start_RecyclerView(){
        prgressBar.setVisibility(View.VISIBLE);
        userArrayList = new ArrayList<>();
        recyclerViewOrders.setHasFixedSize(true);
        recyclerViewOrders.setNestedScrollingEnabled(true);
        recyclerViewOrders.setItemAnimator(new DefaultItemAnimator());
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        databaseReference.push().getKey();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user;
                    user = snapshot.getValue(User.class);
                    userArrayList.add(user);
                }
                myAdapterUser = new MyAdapterUser(userArrayList);
                recyclerViewOrders.setAdapter(myAdapterUser);
                prgressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),R.string.error, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }
}