package com.example.chatapp.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.chatapp.Adapters.MainViewPagerAdapter;
import com.example.chatapp.Models.GroupChat;
import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference chatGroups,userInfo,userChatGroups;
    FirebaseUser currentUser;
    private AlertDialog dialog;
    private TextInputEditText editText;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.main_toolbar);
        tabLayout = findViewById(R.id.main_tab);
        viewPager = findViewById(R.id.main_viewpager);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        chatGroups = firebaseDatabase.getReference("Groups");
        userChatGroups = firebaseDatabase.getReference("UserChatGroup");
        userInfo = firebaseDatabase.getReference("User");
        currentUser = mAuth.getCurrentUser();

        // main toolbar
        setToolBar();

        // main view pager
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        tabLayout.setupWithViewPager(viewPager);

    }


    @SuppressLint("RestrictedApi")
    private void setToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chat App");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.createGroup:
                createChatGroup();
                break;
            case R.id.settings:
                changeUserInfo();
                break;
            case R.id.logout:
                mAuth.signOut();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void changeUserInfo() {

        startActivity(new Intent(MainActivity.this, AddProfileInfoActivity.class));

    }

    private void createChatGroup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Create Group")
                .setPositiveButton("create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addGroupToFireBase();
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        editText = new TextInputEditText(MainActivity.this);
        editText.setHint("Group name");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(params);
        builder.setView(editText);
        dialog = builder.show();
        dialog.setCanceledOnTouchOutside(false);

    }

    private void addGroupToFireBase() {
         String gChat = editText.getText().toString();
        DatabaseReference push = chatGroups.push();
        key = push.getKey();
        GroupChat groupChat = new GroupChat(gChat,key);
        push.setValue(groupChat).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                GroupChat groupChat1 = new GroupChat(editText.getText().toString(),key);
                userChatGroups.child(currentUser.getUid()).child(key).setValue(groupChat1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}