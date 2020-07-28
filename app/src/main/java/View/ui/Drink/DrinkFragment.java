package View.ui.Drink;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testeverythingtwo.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import Model.ModelDrink;
import Model.ModelSweet;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class DrinkFragment extends Fragment {


    @BindView(R.id.imageView_category_drink)
    ImageView imageViewCategoryDrink;
    @BindView(R.id.editText_name_category_drink)
    EditText editTextNameCategoryDrink;
    @BindView(R.id.editText_description_category_drink)
    EditText editTextDescriptionCategoryDrink;
    @BindView(R.id.editText_price_category_drink)
    EditText editTextPriceCategoryDrink;
    @BindView(R.id.button_save_drink)
    Button buttonSaveDrink;
    @BindView(R.id.editText_DeleteName_category_drink)
    EditText editTextDeleteNameCategoryDrink;
    @BindView(R.id.button_delete_category_drink)
    Button buttonDeleteCategoryDrink;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference mStorageRef = storage.getReference();
    Uri imageUri;
    private StorageTask uploadTask;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_drink, container, false);
        ButterKnife.bind(this, root);
        onClick();
        return root;
    }

    private void setFirebase() {
        ModelDrink modelDrink = new ModelDrink();
        String name = editTextNameCategoryDrink.getText().toString();
        String description = editTextDescriptionCategoryDrink.getText().toString();
        String price = editTextPriceCategoryDrink.getText().toString();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name_drink", name);
        hashMap.put("description_drink", description);
        hashMap.put("price_drink", price);
        hashMap.put("image_drink", "default");
        hashMap.put("shrink", modelDrink.isShrink());
        databaseReference = database.getReference("Menu").child("Drink").child(name);
        databaseReference.setValue(hashMap);
    }

    private void onClick() {
        buttonSaveDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFirebase();
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(getContext(), R.string.uploading, Toast.LENGTH_SHORT).show();
                } else {
                    uploadImage();
                }
            }
        });

        imageViewCategoryDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        buttonDeleteCategoryDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deleteName = editTextDeleteNameCategoryDrink.getText().toString();
                databaseReference = database.getReference("Menu").child("Drink");
                databaseReference.child(deleteName).removeValue();
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
    private String getFileExtention(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.uplaodingImage));
        progressDialog.show();
        if (imageUri != null) {
            final StorageReference filereference = mStorageRef
                    .child(editTextNameCategoryDrink.getText().toString())
                    .child(String.valueOf(System.currentTimeMillis())
                            + "." + getFileExtention(imageUri));
            uploadTask = filereference.putFile(imageUri);
            if (imageUri != null) {
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filereference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Uri uri = (Uri) task.getResult();
                            final String image = uri.toString();
                            databaseReference = database.getReference("Menu").child("Drink")
                                    .child(editTextNameCategoryDrink.getText().toString());
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("image_drink", image);
                            databaseReference.updateChildren(hashMap);
                            progressDialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        } else {
            Toast.makeText(getContext(), R.string.chooseImage, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCode && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(imageViewCategoryDrink);
        }
    }
}