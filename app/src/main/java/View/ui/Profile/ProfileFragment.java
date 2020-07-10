package View.ui.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.testeverythingtwo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    @BindView(R.id.edit_profile)
    ImageView editProfile;
    @BindView(R.id.imageView_restaurant)
    CircleImageView imageViewRestaurant;
    @BindView(R.id.textView_restaurantName)
    TextView textViewRestaurantName;
    @BindView(R.id.nameTextLinear)
    LinearLayout nameTextLinear;
    @BindView(R.id.editText_restaurantName)
    EditText editTextRestaurantName;
    @BindView(R.id.nameEditLinear)
    LinearLayout nameEditLinear;
    @BindView(R.id.button_apply_cart)
    Button buttonApplyCart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this,root);

        return root;
    }
}