package com.example.cleansafi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateProfile extends AppCompatActivity {

    LinearLayout personalinfo, history, review;
    TextView personalinfobtn, historybtn, reviewbtn, updateprofilebtn;
    ImageView backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
        setContentView(R.layout.activity_update_profile);

        personalinfo = findViewById(R.id.personalinfo);
        history = findViewById(R.id.experience);
        review = findViewById(R.id.review);
        personalinfobtn = findViewById(R.id.personalinfobtn);
        historybtn = findViewById(R.id.historybtn);
        reviewbtn = findViewById(R.id.reviewbtn);
        updateprofilebtn =findViewById(R.id.update_profilebtn);
        backbtn = findViewById(R.id.backbtn);
        /*making personal info visible*/
        personalinfo.setVisibility(View.VISIBLE);
        history.setVisibility(View.VISIBLE);
        review.setVisibility(View.VISIBLE);



        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(UpdateProfile.this, PlaceOrderActivity.class));

            }
        });


        personalinfobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(UpdateProfile.this, UpdateProfile.class));

            }
        });

        historybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateProfile.this, UpdateProfile.class));

            }
        });

        reviewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateProfile.this, UpdateProfile.class));
            }
        });

        updateprofilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UpdateProfile.this, "Your profile was updated successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpdateProfile.this, PlaceOrderActivity.class));
            }
        });
    }
}