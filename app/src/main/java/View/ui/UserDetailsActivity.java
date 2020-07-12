package View.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.testeverythingtwo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;


public class UserDetailsActivity extends AppCompatActivity {
//    @BindView(R.id.write)
//    Button write;
//    @BindView(R.id.prepare)
//    Button prepare;
//    @BindView(R.id.way)
//    Button way;
//    @BindView(R.id.delivered)
//    Button delivered;
//    @BindView(R.id.switch1)
//    Switch aSwitch;
//    @BindView(R.id.switch2)
//    Switch switch2;
//    @BindView(R.id.switch3)
//    Switch switch3;
//    @BindView(R.id.switch4)
//    Switch switch4;
//    int valueSeekbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);
    }

//    public void onClickButton() {
//        write.setOnClickListener(new View.OnClickListener() {
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
//                            if (writeOrder == progress) {
//                                textMessage.setText(R.string.Your_order_is_written);
//                                orderImage.setImageResource(R.drawable.ic_order_180);
//                                seekBar.setProgress(1);
//                                valueSeekbar = seekBar.getProgress();
//                                databaseReference.child("valueSeekBar").setValue(valueSeekbar);
//                            }
//                            else if (writeOrder != progress){
//                                textMessage.setText("Please! Wait");
//                                orderImage.setImageResource(R.drawable.ic_time_180);
//                                seekBar.setProgress(0);
//                                valueSeekbar = seekBar.getProgress();
//                                databaseReference.child("valueSeekBar").setValue(valueSeekbar);
//                            }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//        prepare.setOnClickListener(new View.OnClickListener() {
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
//                        if (isAdded()){
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
//        way.setOnClickListener(new View.OnClickListener() {
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
//                        if (isAdded()){
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
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//        delivered.setOnClickListener(new View.OnClickListener() {
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
//                        if (isAdded()){
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
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//    }
//
//    private void checkSeekBar() {
//        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        final DatabaseReference databaseReference = database.getReference("Cart").child(userId).child(userId);
//        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
//                if (isChecked) {
//                    databaseReference.child("writeOrder").setValue(true);
//
//                } else {
//                    databaseReference.child("writeOrder").setValue(false);
//                }
//
//            }
//        });
//        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
//                if (isChecked) {
//                    databaseReference.child("preparingOrder").setValue(true);
//
//                } else {
//                    databaseReference.child("preparingOrder").setValue(false);
//                }
//
//            }
//        });
//        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
//                if (isChecked) {
//                    databaseReference.child("wayOrder").setValue(true);
//
//                } else {
//                    databaseReference.child("wayOrder").setValue(false);
//                }
//
//            }
//        });
//        switch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
//                if (isChecked) {
//                    databaseReference.child("deliveredOrder").setValue(true);
//
//                } else {
//                    databaseReference.child("deliveredOrder").setValue(false);
//                }
//
//            }
//        });
//        // To save value is true or false...
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                boolean writeOrder = dataSnapshot.child("writeOrder").getValue(Boolean.class);
//                boolean preparingOrder = dataSnapshot.child("preparingOrder").getValue(Boolean.class);
//                boolean wayOrder = dataSnapshot.child("wayOrder").getValue(Boolean.class);
//                boolean deliveredOrder = dataSnapshot.child("deliveredOrder").getValue(Boolean.class);
//                if (writeOrder == true) {
//                    aSwitch.setChecked(true);
//                } else {
//                    aSwitch.setChecked(false);
//                }
//                if (preparingOrder == true) {
//                    switch2.setChecked(true);
//                } else {
//                    switch2.setChecked(false);
//                }
//                if (wayOrder == true) {
//                    switch3.setChecked(true);
//                } else {
//                    switch3.setChecked(false);
//                }
//                if (deliveredOrder == true) {
//                    switch4.setChecked(true);
//                } else {
//                    switch4.setChecked(false);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    private void saveSeekBarProgress(){
//        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        final DatabaseReference databaseReference = database.getReference("Cart").child(userId)
//                .child(userId).child("valueSeekBar");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                int value = dataSnapshot.getValue(Integer.class);
//                seekBar.setProgress(value);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//    }
}