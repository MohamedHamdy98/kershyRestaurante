package View.ui.Burger;

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

import com.bumptech.glide.Glide;
import com.example.testeverythingtwo.R;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import Model.ModelBurger;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

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
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference mStorageRef = storage.getReference();
    Uri imageUri;
    private StorageTask uploadTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_burger, container, false);
        ButterKnife.bind(this, root);
        onClick();
        return root;
    }

    private void setFirebase() {
        ModelBurger modelBurger = new ModelBurger();
        String name = editTextNameCategoryBurger.getText().toString();
        String description = editTextDescriptionCategoryBurger.getText().toString();
        String price = editTextPriceCategoryBurger.getText().toString();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name_burger", name);
        hashMap.put("description_burger", description);
        hashMap.put("price_burger", price);
        hashMap.put("image_burger", "default");
        hashMap.put("shrink", modelBurger.isShrink());
        databaseReference = database.getReference("Menu").child("Burger").child(name);
        databaseReference.setValue(hashMap);

    }

    private void onClick() {
        buttonSaveBurger.setOnClickListener(new View.OnClickListener() {
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

        imageViewCategoryBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        buttonDeleteCategoryBurger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deleteName = editTextDeleteNameCategoryBurger.getText().toString();
                databaseReference = database.getReference("Menu").child("Burger");
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
                    child(editTextNameCategoryBurger.getText().toString()).
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
                            databaseReference = database.getReference("Menu").child("Burger")
                                    .child(editTextNameCategoryBurger.getText().toString());
//                        databaseReference.child("M").child("Burger").push().getKey();
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("image_burger", mUri);
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
            Picasso.get().load(imageUri).into(imageViewCategoryBurger);
        }
    }

    private void getdata() {
        databaseReference = database.getReference("M").child("Burger");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ModelBurger modelBurger = snapshot.getValue(ModelBurger.class);
                    if (modelBurger.getImage_burger().equals("default")) {
                        imageViewCategoryBurger.setImageResource(R.drawable.ic_photo);
                    } else {
                        Glide.with(getActivity()).load(modelBurger.getImage_burger()).into(imageViewCategoryBurger);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}