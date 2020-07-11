package View.ui.Offers;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
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

import Model.ModelBurger;
import Model.ModelItemOffer;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class OffersFragment extends Fragment {

    @BindView(R.id.imageView_category_offers)
    ImageView imageViewCategoryOffers;
    @BindView(R.id.editText_name_category_offer)
    EditText editTextNameCategoryOffer;
    @BindView(R.id.editText_oldPrice_category_offer)
    EditText editTextOldPriceCategoryOffer;
    @BindView(R.id.editText_newPrice_category_offer)
    EditText editTextNewPriceCategoryOffer;
    @BindView(R.id.button_save_offer)
    Button buttonSaveOffer;
    @BindView(R.id.editText_DeleteName_category_offer)
    EditText editTextDeleteNameCategoryOffer;
    @BindView(R.id.button_delete_category_offer)
    Button buttonDeleteCategoryOffer;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference mStorageRef = storage.getReference();
    Uri imageUri;
    private StorageTask uploadTask;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_offers, container, false);
        ButterKnife.bind(this, root);
        onClick();
        return root;
    }
    private void setFirebase() {
        String name = editTextNameCategoryOffer.getText().toString();
        String oldPrice = editTextOldPriceCategoryOffer.getText().toString();
        String NewPrice = editTextNewPriceCategoryOffer.getText().toString();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name_offer", name);
        hashMap.put("new_price_offer", NewPrice);
        hashMap.put("old_price_offer", oldPrice);
        hashMap.put("image_offer", "default");
        databaseReference = database.getReference("Menu").child("Offers").child(name);
        databaseReference.setValue(hashMap);

    }

    private void onClick() {
        buttonSaveOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFirebase();
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(getContext(), "Uploading...", Toast.LENGTH_SHORT).show();
                } else {
                    uploadImage();
                }
            }
        });

        imageViewCategoryOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        buttonDeleteCategoryOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deleteName = editTextDeleteNameCategoryOffer.getText().toString();
                databaseReference = database.getReference("Menu").child("Offers");
                databaseReference.child(deleteName).removeValue();
            }
        });
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    private String getFileExtention(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        if (imageUri != null) {
            final StorageReference filereference = mStorageRef.
                    child(editTextNewPriceCategoryOffer.getText().toString()).
                    child(String.valueOf(System.currentTimeMillis())
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
                            Uri downloadUri = (Uri) task.getResult();
                            final String mUri = downloadUri.toString();
                            databaseReference = database.getReference("Menu").child("Offers")
                                    .child(editTextNameCategoryOffer.getText().toString());
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("image_offer", mUri);
                            databaseReference.updateChildren(hashMap);
                            progressDialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        } else {
            Toast.makeText(getContext(), "Please! Choose image!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCode && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(imageViewCategoryOffers);
        }
    }

}