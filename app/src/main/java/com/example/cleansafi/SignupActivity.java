package com.example.cleansafi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{

    // Declare variables

    private FirebaseAuth firebaseAuth;

    Button btnsignup;
    EditText inputEmail, inputpassword, inputpassword1;
    LinearLayout linklogin;

    private ProgressDialog progressDialog;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Initialize
        linklogin =(LinearLayout) findViewById(R.id.link_login);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputpassword = (EditText) findViewById(R.id.input_password);
        inputpassword1 = (EditText) findViewById(R.id.input_password1);
        btnsignup = (Button) findViewById(R.id.btn_signup);

        progressDialog = new ProgressDialog(this);

        // Set listeners
        linklogin.setOnClickListener(this);
        btnsignup.setOnClickListener(this);
    }

    // Register user
    private void registerUser(){
        String email = inputEmail.getText().toString().trim();
        String password = inputpassword.getText().toString().trim();
        String confirmPassword = inputpassword1.getText().toString().trim();

        // If email is empty, please enter email
        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        // If password is empty, please enter password
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        // If confirm password is empty, please confirm
        if (TextUtils.isEmpty(confirmPassword) || !password.equals(confirmPassword)){
            Toast.makeText(this, "Please confirm password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering your account. Please wait...");
        progressDialog.show();

        // Create user with email and password
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Successfully registered user, please verify through user email
                                        Toast.makeText(context, "Kindly check your email for verification.", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(context, task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            finish();
                            // Redirect to login activity
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                        else {
                            Toast.makeText(SignupActivity.this,"Could not register you ... Please try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view){
        if (view == btnsignup){
            // Register user
            registerUser();
        }
        else if (view == linklogin){
            finish();
            // Redirect to login activity
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
