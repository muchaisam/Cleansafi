package com.example.cleansafi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cleansafi.Utils.CustomCartAdapter;
import com.example.cleansafi.Utils.DBHelper;
import com.example.cleansafi.Utils.DBModel;
import com.example.cleansafi.Utils.OnItemClick;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CheckOutActivity extends AppCompatActivity implements OnItemClick {
    RecyclerView recyclerView;
    CustomCartAdapter customCartAdapter;
    DBHelper dbHelper;
    List<DBModel> dbModelList;
    List<DBModel> tempList;
    ImageView back_btn;
    int cart_total = 0;
    TextView cart_total_price, cartEmpty;
    RelativeLayout cart_value;
    Button checkout_page;
    List<Serializable> order_no = new ArrayList<Serializable>();



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
        setContentView(R.layout.activity_check_out);
        dbHelper = new DBHelper(this);
        dbModelList = dbHelper.getDataFromDbForHistory();
        tempList = dbHelper.getDataTemp();

        back_btn = findViewById(R.id.backbtn);
        cart_value = findViewById(R.id.cartsss);
        cart_total_price = findViewById(R.id.cart_total_tv);
        cartEmpty = findViewById(R.id.cart_empty);
        checkout_page = findViewById(R.id.checkout_page);
        recyclerView = findViewById(R.id.cart_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (dbModelList.size() > 0) {
            cart_value.setVisibility(View.VISIBLE);
            cartEmpty.setVisibility(View.GONE);

        } else {
            cart_value.setVisibility(View.GONE);
            cartEmpty.setVisibility(View.VISIBLE);

        }

        checkout_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckOutActivity.this, PaymentActivity.class);
                int ordersss = 0;
                for (int i = 0; i < tempList.size(); i++) {

                    order_no.add(tempList.get(i).getId());
                    ordersss = tempList.get(i).getId();
                }
                Log.e("CheckOutActivity", "onClick000: " + ordersss);

                intent.putExtra("order_id", "" + ordersss);
                intent.putExtra("mylist", (Serializable) order_no);
                startActivity(intent);
            }
        });
        customCartAdapter = new CustomCartAdapter(this, dbModelList, this);
        recyclerView.setAdapter(customCartAdapter);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        for (int i = 0; i < dbModelList.size(); i++) {

            cart_total = cart_total + dbModelList.get(i).getFinal_price();
        }

        cart_total_price.setText("Ksh " + cart_total);

        Log.e("msg", "onCreate: Ksh " + cart_total);

    }

    @Override
    public void onClick(List<DBModel> list,int cartvalue) {
        cart_total_price.setText("Ksh");
        cart_total = 0;
        if (list.size() == 0) {
            cart_value.setVisibility(View.GONE);
            cartEmpty.setVisibility(View.VISIBLE);
        } else {

            cart_value.setVisibility(View.VISIBLE);
            cartEmpty.setVisibility(View.GONE);
            for (int i = 0; i < list.size(); i++) {
                cart_total = cart_total + list.get(i).getFinal_price();
            }
            cart_total_price.setText("Ksh " + cart_total);
        }



    }
}