package View.ui.Burger;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.testeverythingtwo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BurgerFragment extends Fragment {


    @BindView(R.id.imageView_category_burger)
    ImageView imageViewCategoryBurger;
    @BindView(R.id.editText_name_category_burger)
    EditText editTextNameCategoryBurger;
    @BindView(R.id.editText_description_category_burger)
    EditText editTextDescriptionCategoryBurger;
    @BindView(R.id.editText_price_category_burger)
    EditText editTextPriceCategoryBurger;
    @BindView(R.id.button_save_burger)
    Button buttonSaveBurger;
    @BindView(R.id.editText_DeleteName_category_burger)
    EditText editTextDeleteNameCategoryBurger;
    @BindView(R.id.button_delete_category_burger)
    Button buttonDeleteCategoryBurger;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_burger, container, false);
        ButterKnife.bind(this,root);
        return root;
    }
    private void setFirebase(){
        databaseReference = database.getReference("Menu");
    }
}