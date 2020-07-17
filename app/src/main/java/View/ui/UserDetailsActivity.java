package View.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testeverythingtwo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Adapter.MyAdapterOrders;
import Adapter.MyAdapterUser;
import Adapter.RecyclerViewClickInterface;
import Model.ModelCart;
import Model.User;
import butterknife.BindView;
import butterknife.ButterKnife;


public class UserDetailsActivity extends AppCompatActivity implements RecyclerViewClickInterface {

    @BindView(R.id.recyclerView_order)
    RecyclerView recyclerViewOrder;
    @BindView(R.id.below)
    CardView below;
    @BindView(R.id.textView_userName)
    TextView textViewUserName;
    @BindView(R.id.textView_userPhone)
    TextView textViewUserPhone;
    @BindView(R.id.textView_userAddress)
    TextView textViewUserAddress;
    @BindView(R.id.textView_userTotalPrice)
    TextView textViewUserTotalPrice;
    @BindView(R.id.textView_userDelivery)
    TextView textViewUserDelivery;
    @BindView(R.id.textView_userTax)
    TextView textViewUserTax;
    @BindView(R.id.textView_userOffers)
    TextView textViewUserOffers;
    @BindView(R.id.textView_totalPriceToPay)
    TextView textViewTotalPriceToPay;
    @BindView(R.id.c)
    CardView c;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference2;
    ArrayList<User> modelUserArrayList = new ArrayList<>();
    MyAdapterOrders myAdapterOrders;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);
        getDataUser();
        start_recyclerView();
    }

    private void start_recyclerView() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        recyclerViewOrder.setHasFixedSize(true);
        recyclerViewOrder.setNestedScrollingEnabled(true);
        recyclerViewOrder.setLayoutManager(new LinearLayoutManager(this));
        FirebaseDatabase fireData = FirebaseDatabase.getInstance();
        String id = getIntent().getExtras().getString("id");
        DatabaseReference databaseReference = fireData.getReference("Cart").child(id).child("Order");
        textViewUserName.setText(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelUserArrayList.clear();
                //for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapData : dataSnapshot.getChildren()) {
                        User user = snapData.getValue(User.class);
                        modelUserArrayList.add(user);
                  //  }
                    myAdapterOrders = new MyAdapterOrders(modelUserArrayList, context);
                    recyclerViewOrder.setAdapter(myAdapterOrders);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        progressDialog.dismiss();
    }

    private void getDataUser() {
        //textViewUserName.setText(getIntent().getExtras().getString("name"));
        textViewUserPhone.setText(getIntent().getExtras().getString("phone"));
        textViewUserAddress.setText(getIntent().getExtras().getString("address"));
        textViewUserTotalPrice.setText(getIntent().getExtras().getString("totalPrice"));
        textViewTotalPriceToPay.setText(getIntent().getExtras().getString("totalPriceToPay"));
        databaseReference2 = database.getReference();
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String tax = dataSnapshot.child("Tax").getValue(String.class);
                String deliveryFee = dataSnapshot.child("deliveryFee").getValue(String.class);
                String offers = dataSnapshot.child("Offers").getValue(String.class);
                textViewUserTax.setText(tax);
                textViewUserDelivery.setText(deliveryFee);
                textViewUserOffers.setText(offers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void saveSeekBarProgress() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference("Cart").child("valueSeekBar");
    }

    @Override
    public void onItemClick(int position) {

    }

//    public void onClickButton () {
//        buttonWrite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                final DatabaseReference databaseReference = database.getReference("Cart")
//                        .child(userId).child(userId);
//                databaseReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        boolean writeOrder = dataSnapshot.child("writeOrder").getValue(boolean.class);
//                        boolean progress = dataSnapshot.child("progress").getValue(boolean.class);
//                        if (writeOrder == progress) {
//                            textMessage.setText(R.string.Your_order_is_written);
//                            orderImage.setImageResource(R.drawable.ic_order_180);
//                            seekBar.setProgress(1);
//                            valueSeekbar = seekBar.getProgress();
//                            databaseReference.child("valueSeekBar").setValue(valueSeekbar);
//                        } else if (writeOrder != progress) {
//                            textMessage.setText("Please! Wait");
//                            orderImage.setImageResource(R.drawable.ic_time_180);
//                            seekBar.setProgress(0);
//                            valueSeekbar = seekBar.getProgress();
//                            databaseReference.child("valueSeekBar").setValue(valueSeekbar);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//        buttonPrepare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                final DatabaseReference databaseReference = database.getReference("Cart").child(userId).child(userId);
//                databaseReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        boolean preparingOrder = dataSnapshot.child("preparingOrder").getValue(Boolean.class);
//                        boolean progress = dataSnapshot.child("progress").getValue(boolean.class);
//                        // databaseReference.child("valueSeekBar").removeValue();
//                        if (isAdded()) {
//                            if (preparingOrder == progress) {
//                                textMessage.setText(R.string.Your_order_is_preparing);
//                                orderImage.setImageResource(R.drawable.ic_cook);
//                                seekBar.setProgress(2);
//                                valueSeekbar = seekBar.getProgress();
//                                //  databaseReference.child("valueSeekBar").setValue(valueSeekbar);
//                            } else if (preparingOrder != progress) {
//                                textMessage.setText(R.string.Your_order_is_written);
//                                orderImage.setImageResource(R.drawable.ic_order_180);
//                                seekBar.setProgress(1);
//                                valueSeekbar = seekBar.getProgress();
//                                //  databaseReference.child("valueSeekBar").setValue(valueSeekbar);
//                            }
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//        buttonWay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                final DatabaseReference databaseReference = database.getReference("Cart").child(userId).child(userId);
//                databaseReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        boolean wayOrder = dataSnapshot.child("wayOrder").getValue(Boolean.class);
//                        boolean progress = dataSnapshot.child("progress").getValue(boolean.class);
//                        // databaseReference.child("valueSeekBar").removeValue();
//                        if (isAdded()) {
//                            if (wayOrder == progress) {
//                                textMessage.setText(R.string.Your_order_is_on_the_way);
//                                orderImage.setImageResource(R.drawable.ic_delivery_180);
//                                seekBar.setProgress(3);
//                                valueSeekbar = seekBar.getProgress();
//                                // databaseReference.child("valueSeekBar").setValue(valueSeekbar);
//                            } else if (wayOrder != progress) {
//                                textMessage.setText(R.string.Your_order_is_preparing);
//                                orderImage.setImageResource(R.drawable.ic_cook);
//                                seekBar.setProgress(2);
//                                valueSeekbar = seekBar.getProgress();
//                                //databaseReference.child("valueSeekBar").setValue(valueSeekbar);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//        buttonDelivered.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                final DatabaseReference databaseReference = database.getReference("Cart").child(userId).child(userId);
//                databaseReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        boolean deliveredOrder = dataSnapshot.child("deliveredOrder").getValue(Boolean.class);
//                        boolean progress = dataSnapshot.child("progress").getValue(boolean.class);
//                        //  databaseReference.child("valueSeekBar").removeValue();
//                        if (isAdded()) {
//                            if (deliveredOrder == progress) {
//                                textMessage.setText(R.string.Your_order_is_receiving);
//                                orderImage.setImageResource(R.drawable.ic_receive_180);
//                                seekBar.setProgress(4);
//                                valueSeekbar = seekBar.getProgress();
//                                // databaseReference.child("valueSeekBar").setValue(valueSeekbar);
//                            } else if (deliveredOrder != progress) {
//                                textMessage.setText(R.string.Your_order_is_on_the_way);
//                                orderImage.setImageResource(R.drawable.ic_delivery_180);
//                                seekBar.setProgress(3);
//                                valueSeekbar = seekBar.getProgress();
//                                // databaseReference.child("valueSeekBar").setValue(valueSeekbar);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//    }
}