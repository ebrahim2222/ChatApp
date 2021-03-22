package com.example.chatapp.Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.chatapp.Models.User;
import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddProfileInfoActivity extends AppCompatActivity {
    CircleImageView userImage;
    TextInputLayout fullNameLayout, userNameLayout,statueLayout;
    TextInputEditText fullNameField, userNameField,statueField;
    Button saveInfo;
    private Uri croppedImage;
    ProgressBar saveProBar;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference saveInfoDatabase;
    FirebaseUser currentUser;
    DatabaseReference checkFirsttime;
    FirebaseStorage firebaseStorage;
    StorageReference saveProfilePicture;
    private String fullName;
    private String name;
    private String statue;
    private User currentUserInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile_info);


        // attributes objects
        userImage = findViewById(R.id.userProfilePic);
        fullNameLayout = findViewById(R.id.fullNameLayout);
        userNameLayout = findViewById(R.id.userNameLayout);
        statueLayout = findViewById(R.id.stateLayout);
        fullNameField = findViewById(R.id.fullFieldField);
        userNameField = findViewById(R.id.userNameField);
        statueField = findViewById(R.id.statueField);
        saveInfo = findViewById(R.id.saveInfo);
        saveProBar = findViewById(R.id.save_probar);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        saveInfoDatabase = firebaseDatabase.getReference();
        currentUser = mAuth.getCurrentUser();
        checkFirsttime = firebaseDatabase.getReference("checkFirstLogin");
        firebaseStorage = FirebaseStorage.getInstance();
        saveProfilePicture = firebaseStorage.getReference("userProfile");

       // methods implementation
        hideProBar();
        getUserInfo();

        // buttons action
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryPermission();
            }
        });

        saveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataOnFireBase();
            }
        });
/*
        userNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                saveInfoDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("User"))
                        {
                            Iterable<DataSnapshot> user = dataSnapshot.child("User").getChildren();
                            for(DataSnapshot ds : user)
                            {
                                String userName = ds.child("userName").getValue().toString();
                                if(userName.equals(name))
                                {
                                    userNameLayout.setError("already used");
                                }
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(AddProfileInfoActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
*/

    }

    // show user data
    private void getUserInfo() {
        saveInfoDatabase.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(currentUser.getUid()))
                {
                    currentUserInfo = dataSnapshot.child(currentUser.getUid()).getValue(User.class);
                    Picasso.with(AddProfileInfoActivity.this).load(currentUserInfo.getUserPhoto()).into(userImage);
                    fullNameField.setText(currentUserInfo.getFullName());
                    userNameField.setText(currentUserInfo.getUserName());
                    statueField.setText(currentUserInfo.getUserStatue());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    // enable progress bar
    public void showProBar()
    {
        saveInfo.setVisibility(View.INVISIBLE);
        saveProBar.setVisibility(View.VISIBLE);
    }

    //unenable progress bar
    public void hideProBar()
    {
        saveInfo.setVisibility(View.VISIBLE);
        saveProBar.setVisibility(View.INVISIBLE);
    }


    // gallery permission
    private void galleryPermission() {
        if(ContextCompat.checkSelfPermission(AddProfileInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(AddProfileInfoActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE))
            {

            }else
            {
                ActivityCompat.requestPermissions(AddProfileInfoActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
        }else
        {
            openGallery();
        }
    }

    // open crop image library to take select image
    private void openGallery() {

        CropImage.startPickImageActivity(AddProfileInfoActivity.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

                    Uri pickImageResultUri = CropImage.getPickImageResultUri(AddProfileInfoActivity.this, data);
                    CropImage.activity(pickImageResultUri).setGuidelines(CropImageView.Guidelines.ON).start(AddProfileInfoActivity.this);
                    if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
                    {
                        CropImage.ActivityResult result = CropImage.getActivityResult(data);
                        croppedImage = result.getUri();
                        userImage.setImageURI(croppedImage);
                    }

    }


    // when on click to save user info
    private void saveDataOnFireBase() {

          fullName = fullNameField.getText().toString();
          name = userNameField.getText().toString();
          statue = statueField.getText().toString();
        if(TextUtils.isEmpty(fullName) && TextUtils.isEmpty(name) && TextUtils.isEmpty(statue)) {
            Toast.makeText(AddProfileInfoActivity.this, "please, enter all field", Toast.LENGTH_LONG).show();

        } else if(TextUtils.isEmpty(fullName) && !TextUtils.isEmpty(name)&& !TextUtils.isEmpty(statue))
        {
            Toast.makeText(AddProfileInfoActivity.this, "please, enter your full name ", Toast.LENGTH_LONG).show();

        }else if(!TextUtils.isEmpty(fullName) && TextUtils.isEmpty(name)&& !TextUtils.isEmpty(statue))
        {
            Toast.makeText(AddProfileInfoActivity.this, "please, enter your username ", Toast.LENGTH_LONG).show();
        }else if(!TextUtils.isEmpty(fullName) && !TextUtils.isEmpty(name)&& TextUtils.isEmpty(statue))
        {
            Toast.makeText(AddProfileInfoActivity.this, "please, enter your statue ", Toast.LENGTH_LONG).show();
        }
        else
        {
            showProBar();
            saveInfoDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild("User"))
                    {
                        Iterable<DataSnapshot> user = dataSnapshot.child("User").getChildren();
                        for(DataSnapshot ds : user)
                        {
                            String userName = ds.child("userName").getValue().toString();
                            if(name.equals(userName))
                            {
                                userNameLayout.setError("already used");
                                hideProBar();
                            }
                            else
                            {
                                userNameLayout.setError(null);
                               if(croppedImage.equals(null))
                               {
                                   // save use with out image in firebase
                                   addUserWithDefaultImageFromFirebase();
                               }else
                               {
                                   // save use with image in firebase
                                   addUserWithSelectedImageFromDatabase();
                               }
                            }
                        }

                    }else
                    {
                        if(croppedImage == null)
                        {
                            // save use with image from firebase in firebase again
                            addUserWithDefaultImage();
                        }else
                        {
                            // save use with image in firebase
                            addUserWithSelectedImage();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AddProfileInfoActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void addUserWithSelectedImage() {
        saveProfilePicture.child(currentUser.getUid()).child(currentUser.getUid()).putFile(croppedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        User user = new User(currentUser.getUid(),currentUser.getEmail(),name,statue,uri.toString(),fullName);
                        saveInfoDatabase.child("User").child(currentUser.getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                checkFirsttime.child(currentUser.getUid()).child("checkFirstTime").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        startActivity(new Intent(AddProfileInfoActivity.this,MainActivity.class));
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddProfileInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                hideProBar();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddProfileInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        hideProBar();
                    }
                });
            }
        });
    }


    private void addUserWithDefaultImage() {
        User user = new User(currentUser.getUid(),currentUser.getEmail(),name,statue,currentUserInfo.getUserPhoto(),fullName);
        saveInfoDatabase.child("User").child(currentUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                checkFirsttime.child(currentUser.getUid()).child("checkFirstTime").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(AddProfileInfoActivity.this,MainActivity.class));
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddProfileInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                hideProBar();
            }
        });
    }

    private void addUserWithSelectedImageFromDatabase() {
        saveProfilePicture.child(currentUser.getUid()).child(currentUser.getUid()).putFile(croppedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        User user = new User(currentUser.getUid(),currentUser.getEmail(),name,statue,uri.toString(),fullName);
                        saveInfoDatabase.child("User").child(currentUser.getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                checkFirsttime.child(currentUser.getUid()).child("checkFirstTime").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        startActivity(new Intent(AddProfileInfoActivity.this,MainActivity.class));
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddProfileInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                hideProBar();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddProfileInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        hideProBar();
                    }
                });
            }
        });
    }
    private void addUserWithDefaultImageFromFirebase() {
        User user = new User(currentUser.getUid(),currentUser.getEmail(),name,statue,currentUserInfo.getUserPhoto(),fullName);
        saveInfoDatabase.child("User").child(currentUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                checkFirsttime.child(currentUser.getUid()).child("checkFirstTime").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(AddProfileInfoActivity.this,MainActivity.class));
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddProfileInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                hideProBar();
            }
        });
    }

}