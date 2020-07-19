package View.ui.Orders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testeverythingtwo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapter.MyAdapterOrders;
import Adapter.MyAdapterUser;
import Adapter.RecyclerViewClickInterface;
import Model.User;
import View.ui.UserDetailsActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OrdersFragment extends Fragment {
    @BindView(R.id.recyclerView_orders)
    RecyclerView recyclerViewOrders;
    private MyAdapterUser myAdapterUser;
    private ArrayList<User> userArrayList;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference("Cart");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_orders, container, false);
        ButterKnife.bind(this,root);
        start_RecyclerView();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        return root;
    }
    private void start_RecyclerView(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
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
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}