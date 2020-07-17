package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

import Model.User;
import View.ui.UserDetailsActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAdapterUser extends RecyclerView.Adapter<MyAdapterUser.ViewHolder> {

    private Context context;

    private ArrayList<User> modelArrayList = new ArrayList<>();

    private RecyclerViewClickInterface recyclerViewClickInterface;

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
                final Intent intent = new Intent(v.getContext(), UserDetailsActivity.class);
//                FirebaseDatabase fireData = FirebaseDatabase.getInstance();
//                DatabaseReference databaseReference = fireData.getReference("Order");
//                databaseReference.push().getKey();
//                databaseReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        modelArrayList.clear();
//                       // for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            for (DataSnapshot snapData : dataSnapshot.getChildren()) {
//                                User user1 = snapData.getValue(User.class);
//                                modelArrayList.add(user1);
//                            //}
//                        }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                    }
//                });
                intent.putExtra("id",modelArrayList.get(position).getId());
                intent.putExtra("name", modelArrayList.get(position).getUserName());
                intent.putExtra("phone", modelArrayList.get(position).getPhone());
                intent.putExtra("address", modelArrayList.get(position).getAddressWrite());
                intent.putExtra("totalPrice", modelArrayList.get(position).getTotalPrice());
                intent.putExtra("totalPriceToPay", modelArrayList.get(position).getTotalPriceToPay());
                v.getContext().startActivity(intent);
            }
        });
        holder.switchViewUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.cardViewUserDelivery.setVisibility(View.VISIBLE);
                } else {
                    holder.cardViewUserDelivery.setVisibility(View.GONE);
                }
            }
        });

        checkSeekBar(holder, position);
        //controlDelivery(holder);
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
        @BindView(R.id.switch_viewUser)
        Switch switchViewUser;
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
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    private void controlDelivery(final ViewHolder holder) {
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference2;
        // To save value is true or false...
        databaseReference2 = data.getReference("Cart");
        databaseReference2.push().getKey();
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    boolean writeOrder = snapshot.child("writeOrder").getValue(Boolean.class);
                    boolean preparingOrder = snapshot.child("preparingOrder").getValue(Boolean.class);
                    boolean wayOrder = snapshot.child("wayOrder").getValue(Boolean.class);
                    boolean deliveredOrder = snapshot.child("deliveredOrder").getValue(Boolean.class);
                    if (writeOrder == true) {
                        holder.switchWrite.setChecked(true);
                    } else {
                        holder.switchWrite.setChecked(false);
                    }
                    if (preparingOrder == true) {
                        holder.switchPrepare.setChecked(true);
                    } else {
                        holder.switchPrepare.setChecked(false);
                    }
                    if (wayOrder == true) {
                        holder.switchOnTheWay.setChecked(true);
                    } else {
                        holder.switchOnTheWay.setChecked(false);
                    }
                    if (deliveredOrder == true) {
                        holder.switchDelivery.setChecked(true);
                    } else {
                        holder.switchDelivery.setChecked(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkSeekBar(final ViewHolder holder, int position) {
        final User user = modelArrayList.get(position);
        FirebaseDatabase data = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = data.getReference("Cart");
        //databaseReference.push().getKey();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final String key = snapshot.getKey();
                    holder.switchWrite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                            if (isChecked) {
                                databaseReference.child(key).child("writeOrder").setValue(true);
                                databaseReference.child(key).child("progress").setValue(true);
                            } else {
                                databaseReference.child(key).child("writeOrder").setValue(false);
                            }
                        }
                    });
                    holder.switchPrepare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                            if (isChecked) {
                                databaseReference.child(key).child("preparingOrder").setValue(true);

                            } else {
                                databaseReference.child(key).child("preparingOrder").setValue(false);
                            }

                        }
                    });
                    holder.switchOnTheWay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                            if (isChecked) {
                                databaseReference.child(key).child("wayOrder").setValue(true);

                            } else {
                                databaseReference.child(key).child("wayOrder").setValue(false);
                            }

                        }
                    });
                    holder.switchDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                            if (isChecked) {
                                databaseReference.child(key).child("deliveredOrder").setValue(true);

                            } else {
                                databaseReference.child(key).child("deliveredOrder").setValue(false);
                            }

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

}
