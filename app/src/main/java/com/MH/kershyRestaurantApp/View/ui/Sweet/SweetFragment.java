package com.MH.kershyRestaurantApp.View.ui.Sweet;

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

import com.MH.kershyRestaurantApp.R;
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

import com.MH.kershyRestaurantApp.Model.ModelSweet;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class SweetFragment extends Fragment {

    @BindView(R.id.imageView_category_sweet)
    ImageView imageViewCategorySweet;
    @BindView(R.id.editText_name_category_sweet)
    EditText editTextNameCategorySweet;
    @BindView(R.id.editText_description_category_sweet)
    EditText editTextDescriptionCategorySweet;
    @BindView(R.id.editText_price_category_sweet)
    EditText editTextPriceCategorySweet;
    @BindView(R.id.button_save_sweet)
    Button buttonSaveSweet;
    @BindView(R.id.editText_DeleteName_category_sweet)
    EditText editTextDeleteNameCategorySweet;
    @BindView(R.id.button_delete_category_sweet)
    Button buttonDeleteCategorySweet;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference mStorageRef = storage.getReference();
    Uri imageUri;
    private StorageTask uploadTask;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sweet, container, false);
        ButterKnife.bind(this, root);
        onClick();
        return root;
    }

    private void setFirebase() {
        ModelSweet modelSweet = new ModelSweet();
        String name = editTextNameCategorySweet.getText().toString();
        String description = editTextDescriptionCategorySweet.getText().toString();
        String price = editTextPriceCategorySweet.getText().toString();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name_sweet", name);
        hashMap.put("description_sweet", description);
        hashMap.put("price_sweet", price);
        hashMap.put("image_sweet", "default");
        hashMap.put("shrink", modelSweet.isShrink());
        databaseReference = database.getReference("Menu").child("Sweet").child(name);
        databaseReference.setValue(hashMap);
    }

    private void onClick() {
        buttonSaveSweet.setOnClickListener(new View.OnClickListener() {
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

        imageViewCategorySweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        buttonDeleteCategorySweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deleteName = editTextDeleteNameCategorySweet.getText().toString();
                databaseReference = database.getReference("Menu").child("Sweet");
                databaseReference.child(deleteName).removeValue();
            }
        });
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
                    .child(editTextNameCategorySweet.getText().toString())
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
                            Uri downloadUri = (Uri) task.getResult();
                            final String mUri = downloadUri.toString();
                            databaseReference = database.getReference("Menu").child("Sweet")
                                    .child(editTextNameCategorySweet.getText().toString());
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("image_sweet", mUri);
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
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
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
            Picasso.get().load(imageUri).into(imageViewCategorySweet);
        }
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