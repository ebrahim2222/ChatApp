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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout emailFieldLayout,passwordFieldLayout;
    TextInputEditText emailField,passwordField;
    Button reg_btn;
    TextView goToLogin;
    ProgressBar regProBar;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference checkFirsttime;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // firebase objects
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        checkFirsttime = firebaseDatabase.getReference("checkFirstLogin");

        // attributes objects
        emailFieldLayout = findViewById(R.id.emailFieldLayout);
        passwordFieldLayout = findViewById(R.id.passwordFieldLayout);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        reg_btn = findViewById(R.id.reg_btn);
        goToLogin = findViewById(R.id.goToLogin);
        regProBar = findViewById(R.id.reg_probar);


        // methods implementation
        emailHints();
        passwordHints();
        passwordCharacterCounter();
        hideProgressBar();

        // buttons click
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();
                registerButton();

            }
        });
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginPage();
            }
        });



    }


    // hide progress bar
    private void hideProgressBar() {
        regProBar.setVisibility(View.INVISIBLE);
        reg_btn.setVisibility(View.VISIBLE);
    }


    // show progress bar
    private void showProgressBar()
    {
        regProBar.setVisibility(View.VISIBLE);
        reg_btn.setVisibility(View.INVISIBLE);
    }


    // add email hint by click on email
    private void emailHints() {
        emailField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    emailFieldLayout.setHint("email");
                }else
                {
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

    // add user to application
    private void registerButton() {

        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, " Enter all fields ", Toast.LENGTH_LONG).show();
            hideProgressBar();
        }
        else if(password.length()>passwordFieldLayout.getCounterMaxLength())
        {
            passwordFieldLayout.setError("Max character length is 8");
            hideProgressBar();
        }
        else if(password.length()>passwordFieldLayout.getCounterMaxLength())
        {
            passwordFieldLayout.setError("Max character length is 8");
            hideProgressBar();
        }else if(!email.contains("@") && !email.contains(".com"))
        {
            emailFieldLayout.setError("Please enter correct email");
            hideProgressBar();
        }else
        {
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            checkFirsttime.child(currentUser.getUid()).child("checkFirstTime").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(RegisterActivity.this, "Please verify your account", Toast.LENGTH_SHORT).show();
                                    goToLoginPage();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    hideProgressBar();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
    }

    // open login activity

    private void goToLoginPage() {

        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

    }

}