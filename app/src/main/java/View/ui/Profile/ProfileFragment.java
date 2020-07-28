package View.ui.Profile;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.testeverythingtwo.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;

import Model.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

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
    DatabaseReference reference;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference mStorageRef = firebaseStorage.getReference();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    Uri imageUri;
    private StorageTask uploadTask;
    View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this,root);
        onClickEditProfile();
        onClickApply();
        getDataFirebase();
        editImageUser();
        return root;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(getContext(), R.string.uploading, Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.uplaodingImage));
        progressDialog.show();
        if (imageUri != null) {
            final StorageReference filereference = mStorageRef.
                    child(String.valueOf(System.currentTimeMillis())
                            + "." + getFileExtention(imageUri));
            uploadTask = filereference.putFile(imageUri);
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
                        String mUri = downloadUri.toString();
                        reference = FirebaseDatabase.getInstance().getReference("RestaurantInformation")
                                .child(userId);
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("imageURL", mUri);
                        reference.updateChildren(hashMap);
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
        } else {
            Toast.makeText(getContext(), R.string.chooseImage, Toast.LENGTH_SHORT).show();
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
    private void onClickApply() {
        buttonApplyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference("RestaurantInformation").child(userId);
                String name = editTextRestaurantName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Snackbar.make(v, R.string.enterRestaurantName, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                else {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("userName", name);
                    Toast.makeText(getActivity(), R.string.updateIsDone, Toast.LENGTH_SHORT).show();
                    reference.updateChildren(hashMap);
                }
                nameTextLinear.setVisibility(View.VISIBLE);
                nameEditLinear.setVisibility(View.GONE);
                editTextRestaurantName.getText().clear();
                buttonApplyCart.setVisibility(View.GONE);
            }
        });
    }

    private void onClickEditProfile() {
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameTextLinear.setVisibility(View.GONE);
                buttonApplyCart.setVisibility(View.VISIBLE);
                nameEditLinear.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getDataFirebase() {
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        reference = FirebaseDatabase.getInstance().getReference("RestaurantInformation").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (isAdded()){
                    User users = dataSnapshot.getValue(User.class);
                    String nameUser = dataSnapshot.child("userName").getValue(String.class);
                    textViewRestaurantName.setText(nameUser);
                    if (users.getImageURL().equals("default")) {
                        imageViewRestaurant.setImageResource(R.drawable.ic_man);
                    } else {
                        Glide.with(getActivity()).load(users.getImageURL()).into(imageViewRestaurant);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void editImageUser() {
        imageViewRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });
    }
}