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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    // Declare variables

    private FirebaseAuth firebaseAuth;

    EditText input_email;
    EditText input_password;
    Button btn_login;
    TextView link_forgotpassword;
    LinearLayout link_signup;

    private ProgressDialog progressDialog;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        link_signup = (LinearLayout) findViewById(R.id.link_signup);
        link_forgotpassword = (TextView) findViewById(R.id.link_forgotpassword);
        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);
        btn_login = (Button) findViewById(R.id.btn_login);

        progressDialog = new ProgressDialog(this);

        // Set Listeners
        link_signup.setOnClickListener(this);
        link_forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));

            }
        });
        btn_login.setOnClickListener(this);
    }

    private void userLogin(){
        String email = input_email.getText().toString().trim();
        String password  = input_password.getText().toString().trim();

        // If email is empty, return
        if (TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter a valid email",Toast.LENGTH_LONG).show();
            return;
        }

        // If email is empty, return
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Hang on as we log you in.....");
        progressDialog.show();

        // Sign in with email and password
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            // If email is not verified, verify
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (!user.isEmailVerified()){
                                Toast.makeText(LoginActivity.this, "Please Verify your email first.",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                // start main activity
                                finish();
                                Toast.makeText(LoginActivity.this, "Welcome to Cleansafi. Place your order now.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        }
                        else {
                            // Failed to log in
                            Toast.makeText(LoginActivity.this, "Authentication failed.Please Try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view){
        if (view == btn_login){
            userLogin();
        }
        else if (view == link_signup){
            finish();
            startActivity(new Intent(this, SignupActivity.class ));
        }
        else if (view == link_forgotpassword){
            // Reset password through email
            firebaseAuth.getInstance().sendPasswordResetEmail("muchai.samson10@gmail.com")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Email Sent", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}
