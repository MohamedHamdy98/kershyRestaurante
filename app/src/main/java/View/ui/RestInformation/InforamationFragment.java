package View.ui.RestInformation;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.testeverythingtwo.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InforamationFragment extends Fragment {


    @BindView(R.id.button_save)
    Button buttonSave;
    @BindView(R.id.editText_tax)
    EditText editTextTax;
    @BindView(R.id.editText_deliverFee)
    EditText editTextDeliverFee;
    @BindView(R.id.editText_offer)
    EditText editTextOffer;
    @BindView(R.id.editText_timeDelivery)
    EditText editTextTimeDelivery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_information, container, false);
        ButterKnife.bind(this, root);
        onClick();
        return root;
    }

    private void onClick() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tax = editTextTax.getText().toString();
                String fee = editTextDeliverFee.getText().toString();
                String offer = editTextOffer.getText().toString();
                String time = editTextTimeDelivery.getText().toString();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference();
                databaseReference.child("Tax").setValue(tax);
                databaseReference.child("deliveryFee").setValue(fee);
                databaseReference.child("Offers").setValue(offer);
                databaseReference.child("TimeDelivery").setValue(time);
                Snackbar.make(v,"Success",Snackbar.LENGTH_SHORT).show();
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