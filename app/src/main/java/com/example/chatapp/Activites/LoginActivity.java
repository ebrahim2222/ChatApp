package com.example.chatapp.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout emailFieldLayout,passwordFieldLayout;
    TextInputEditText emailField,passwordField;
    Button log_btn;
    TextView goToReg;
    ProgressBar loginProBar;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference checkFirsttime,checkEmail;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // firebase objects
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        checkFirsttime = firebaseDatabase.getReference("checkFirstLogin");
        checkEmail = firebaseDatabase.getReference();


        // attributes objects
        emailFieldLayout = findViewById(R.id.emailFieldLoginLayout);
        passwordFieldLayout = findViewById(R.id.passwordFieldLoginLayout);
        emailField = findViewById(R.id.emailFieldLogin);
        passwordField = findViewById(R.id.passwordFieldLogin);
        log_btn = findViewById(R.id.login_btn);
        goToReg = findViewById(R.id.goToReg);
        loginProBar = findViewById(R.id.loginProBar);

        hideProBar();

        // methods implementation
        emailHints();
        passwordHints();
        passwordCharacterCounter();


        log_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProBar();
                loginButton();
            }
        });

        goToReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegPage();
            }
        });

    }

    // show progress bar

    public void showProBar()
    {
        loginProBar.setVisibility(View.VISIBLE);
        log_btn.setVisibility(View.INVISIBLE);
    }
    // hide progress bar

    public void hideProBar()
    {
        loginProBar.setVisibility(View.INVISIBLE);
        log_btn.setVisibility(View.VISIBLE);
    }

    // add email hint by click on email
    private void emailHints() {
        emailField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    emailFieldLayout.setHint("email");
                } else {
                    emailFieldLayout.setHint("enter your email");
                }
            }
        });


    }

    // add email hint by click on password
    private void passwordHints() {
        passwordField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    passwordFieldLayout.setHint("password");
                }else
                {
                    passwordFieldLayout.setHint("enter your password");
                }
            }
        });

    }

    // check size of password

    private void passwordCharacterCounter() {

        passwordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length() > passwordFieldLayout.getCounterMaxLength())
                {
                    passwordFieldLayout.setError("Max character length is 8");
                }else
                {
                    passwordFieldLayout.setError(null);
                }

            }
        });

    }

    // log in with current user
    private void loginButton() {

         email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password))
        {
            Toast.makeText(LoginActivity.this, " Enter all fields ", Toast.LENGTH_LONG).show();
            hideProBar();
        }else if(TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "please enter your email", Toast.LENGTH_LONG).show();
            hideProBar();
        }else if(!TextUtils.isEmpty(email) && TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "please enter your password", Toast.LENGTH_LONG).show();
            hideProBar();
        }
        else if(password.length()>passwordFieldLayout.getCounterMaxLength())
        {
            passwordFieldLayout.setError("Max character length is 8");
            hideProBar();
        }else if(!email.contains("@") && !email.contains(".com"))
        {
            emailFieldLayout.setError("Please enter correct email");
            hideProBar();
        }else
        {
            mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    if(currentUser.isEmailVerified())
                    {
                        checkFirsttime.child(currentUser.getUid()).child("checkFirstTime").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Boolean value = (Boolean)dataSnapshot.getValue();
                                if(value.equals(false))
                                {
                                    Intent intent = new Intent(LoginActivity.this,AddProfileInfoActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else
                                {
                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(LoginActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                hideProBar();
                            }
                        });
                    }else
                    {
                        Toast.makeText(LoginActivity.this, "you should verify your account", Toast.LENGTH_LONG).show();
                        hideProBar();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    hideProBar();
                }
            });
        }
    }



    // open register activity
    private void goToRegPage() {

        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));

    }


}