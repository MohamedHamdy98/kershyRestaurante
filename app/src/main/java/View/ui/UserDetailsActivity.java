package View.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import Model.User;
import butterknife.BindView;
import butterknife.ButterKnife;


public class UserDetailsActivity extends AppCompatActivity {

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
    @BindView(R.id.button_write)
    Button buttonWrite;
    @BindView(R.id.switch_write)
    Switch switchWrite;
    @BindView(R.id.button_prepare)
    Button buttonPrepare;
    @BindView(R.id.switch_prepare)
    Switch switchPrepare;
    @BindView(R.id.button_way)
    Button buttonWay;
    @BindView(R.id.switch_onTheWay)
    Switch switchOnTheWay;
    @BindView(R.id.button_delivered)
    Button buttonDelivered;
    @BindView(R.id.switch_delivery)
    Switch switchDelivery;
    @BindView(R.id.cardView_user_delivery)
    CardView cardViewUserDelivery;
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
        checkSeekBar();
        controlDelivery();
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
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelUserArrayList.clear();
                for (DataSnapshot snapData : dataSnapshot.getChildren()) {
                    User user = snapData.getValue(User.class);
                    modelUserArrayList.add(user);
                    myAdapterOrders = new MyAdapterOrders(modelUserArrayList, context);
                    recyclerViewOrder.setAdapter(myAdapterOrders);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void controlDelivery() {
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference2;
        // To save value is true or false...
        String id = getIntent().getExtras().getString("id");
        databaseReference2 = data.getReference("Cart").child(id);
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean writeOrder = dataSnapshot.child("writeOrder").getValue(Boolean.class);
                boolean preparingOrder = dataSnapshot.child("preparingOrder").getValue(Boolean.class);
                boolean wayOrder = dataSnapshot.child("wayOrder").getValue(Boolean.class);
                boolean deliveredOrder = dataSnapshot.child("deliveredOrder").getValue(Boolean.class);
                if (writeOrder == true) {
                    switchWrite.setChecked(true);
                } else {
                    switchWrite.setChecked(false);
                }
                if (preparingOrder == true) {
                    switchPrepare.setChecked(true);
                } else {
                    switchPrepare.setChecked(false);
                }
                if (wayOrder == true) {
                    switchOnTheWay.setChecked(true);
                } else {
                    switchOnTheWay.setChecked(false);
                }
                if (deliveredOrder == true) {
                    switchDelivery.setChecked(true);
                } else {
                    switchDelivery.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkSeekBar() {
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        String id = getIntent().getExtras().getString("id");
        final DatabaseReference dataRef = database.getReference("Cart")
                .child(id).child("valueSeekBar");
        final DatabaseReference databaseReference = data.getReference("Cart").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                switchWrite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                        if (isChecked) {
                            databaseReference.child("writeOrder").setValue(true);
                            databaseReference.child("progress").setValue(true);
                            dataRef.setValue(1);
                        } else {
                            databaseReference.child("writeOrder").setValue(false);
                            dataRef.setValue(0);
                        }

                    }
                });
                switchPrepare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                        if (isChecked) {
                            databaseReference.child("preparingOrder").setValue(true);
                            dataRef.setValue(2);
                        } else {
                            databaseReference.child("preparingOrder").setValue(false);
                            dataRef.setValue(1);
                        }

                    }
                });
                switchOnTheWay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                        if (isChecked) {
                            databaseReference.child("wayOrder").setValue(true);
                            dataRef.setValue(3);
                        } else {
                            databaseReference.child("wayOrder").setValue(false);
                            dataRef.setValue(2);
                        }
                    }
                });
                switchDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                        if (isChecked) {
                            databaseReference.child("deliveredOrder").setValue(true);
                            dataRef.setValue(4);
                        } else {
                            databaseReference.child("deliveredOrder").setValue(false);
                            dataRef.setValue(3);
                        }

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getDataUser() {
        textViewUserName.setText(getIntent().getExtras().getString("name"));
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
        String id = getIntent().getExtras().getString("id");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference("Cart")
                .child(id).child("valueSeekBar");

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