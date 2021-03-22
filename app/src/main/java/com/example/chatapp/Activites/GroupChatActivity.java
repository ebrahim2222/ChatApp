package com.example.chatapp.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chatapp.Models.GroupChat;
import com.example.chatapp.Models.Message;
import com.example.chatapp.Models.User;
import com.example.chatapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {
    Toolbar toolbar;
    private GroupChat groupChat;
    TextInputLayout messageFieldLayout;
    TextInputEditText messageField;
    RecyclerView groupChatMessageRv;
    ImageView sendMessage;
    String message;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference saveInfoDatabase;
    FirebaseUser currentUser;
    DatabaseReference groupMessages;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        toolbar = findViewById(R.id.main_toolbar);
        messageFieldLayout = findViewById(R.id.group_chat_messageLayout);
        messageField = findViewById(R.id.group_chat_messageField);
        groupChatMessageRv = findViewById(R.id.group_chat_rv);
        sendMessage = findViewById(R.id.group_chat_sendMessage);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        saveInfoDatabase = firebaseDatabase.getReference();
        currentUser = mAuth.getCurrentUser();
        groupMessages = firebaseDatabase.getReference("groupMessages");
        getCurrentUserData();
        getGroupChatName();
        setToolbar();

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy hh:mm" );
                String format = sdf.format(new Date());
                Message message = new Message(messageField.getText().toString(),format,user.getUserName(),user.getUserPhoto());
                groupMessages.child(groupChat.getGroupKey()).push().setValue(message).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(GroupChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void getCurrentUserData() {
        saveInfoDatabase = firebaseDatabase.getReference("User");
        saveInfoDatabase.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void getGroupChatName() {
        Intent intent = getIntent();
         groupChat = intent.getExtras().getParcelable("groupChat");

    }

    @SuppressLint("RestrictedApi")
    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(groupChat.getGroupName());
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}