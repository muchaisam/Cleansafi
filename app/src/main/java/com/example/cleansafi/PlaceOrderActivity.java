package com.example.cleansafi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.cleansafi.Utils.DBHelper;
import com.example.cleansafi.Utils.DBModel;

import java.util.ArrayList;
import java.util.List;

public class PlaceOrderActivity extends AppCompatActivity {
    int topCount = 0;
    int lowerCount = 0;
    int bedsheetCount = 0;
    int otherCount = 0;
    public RadioGroup radioService;
    public RadioButton radiobtn;
    DatePicker dp;
    TextView topQty, lowerQty, bedsheetQty, otherQty, topPrice, lowerPrice, bedsheetPrice, otherPrice;
    TimePicker tp;
    DBHelper dbHelper;
    Button add_to_bucket;
    int wash = 0;
    int iron = 0;
    int do_both = 0;
    int cart_total = 0;
    int month, date, year, hours, minutes;
    List<DBModel> dbModelList = new ArrayList<>();
    ImageView back_btn, cart_items_place, profile_btn;
    String time;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
//        db
        dbHelper = new DBHelper(this);
        back_btn = findViewById(R.id.backbtn);
        cart_items_place = findViewById(R.id.cart_items_place);
        profile_btn = findViewById(R.id.go_to_profile_p);
        topQty = findViewById(R.id.topQty);
        lowerQty = findViewById(R.id.lowerQty);
        bedsheetQty = findViewById(R.id.bedsheetQty);
        otherQty = findViewById(R.id.otherQty);
        topPrice = findViewById(R.id.topPrice);
        lowerPrice = findViewById(R.id.lowerPrice);
        bedsheetPrice = findViewById(R.id.bedsheetPrice);
        otherPrice = findViewById(R.id.otherPrice);
        radioService = findViewById(R.id.radioService);
        dp = findViewById(R.id.date_picker);
        tp = findViewById(R.id.time_picker);
        add_to_bucket = findViewById(R.id.add_to_bucket);
        dbModelList = dbHelper.getDataFromDbForHistory();

        if (radioService.getCheckedRadioButtonId() == R.id.washRB) {
            wash = 1;
        }
        for (int i = 0; i < dbModelList.size(); i++) {
            Log.e("msg", "onCreate: " + dbModelList.get(i).getFinal_price() + "\n" + dbModelList.get(i).getTop_clothes() + "\n" + dbModelList.get(i).getBedsheets());
        }
        year = dp.getYear();
        month = dp.getMonth();
        date = dp.getDayOfMonth();
        cart_items_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PlaceOrderActivity.this, CheckOutActivity.class));
            }
        });
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PlaceOrderActivity.this, UpdateProfile.class));

            }
        });
        dp.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int Year, int Month, int Date) {
                Log.e("msg", "onDateChanged: " + Year + "\n" + Month + "\n" + Date);
                year = Year;
                month = Month;
                date = Date;
            }
        });
        hours = tp.getHour();
        minutes = tp.getMinute();
        tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int mints) {
                Log.e("msg", "onTimeChanged: " + hour + "\n" + mints);
                hours = hour;
                minutes = mints;
            }
        });

        add_to_bucket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int top = Integer.parseInt(topPrice.getText().toString());
                int lower = Integer.parseInt(lowerPrice.getText().toString());
                int bed_sheets = Integer.parseInt(bedsheetPrice.getText().toString());
                int others = Integer.parseInt(otherPrice.getText().toString());
                String day = date + "/" + month + "/" + year;
                if (hours == 0) {
                    hours = 12;
                    time = hours + ":" + minutes;

                } else {
                    time = hours + ":" + minutes;

                }
                Log.e("msg", "onClick: " + top + "\n" + lower + "\n" + bed_sheets + "\n" + others + "\n" + day + "\n" + time);
                cart_total = top + lower + bed_sheets + others;
                if (top == 0 && lower == 0 && bed_sheets == 0 && others == 0) {
                    Toast.makeText(PlaceOrderActivity.this, "cart can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    dbHelper.insert(top, lower, bed_sheets, others,
                            wash, iron, do_both, cart_total, day, time);
                    dbHelper.insertTemp(top, lower, bed_sheets, others,
                            wash, iron, do_both, cart_total, day, time);
                    SharedPreferences sp = getApplicationContext().getSharedPreferences("CleanSafi", Context.MODE_PRIVATE);
                    dbHelper.insertUserOrder(sp.getString("email", ""), top, lower, bed_sheets, others,
                            wash, iron, do_both, cart_total, day, time);
                }
                startActivity(new Intent(PlaceOrderActivity.this, CheckOutActivity.class));

            }
        });

    }

    public void clickradioButton(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.ironRB:
                if (checked) {
                    displayTopPrice(topCount * 3);
                    displayLowerPrice(lowerCount * 3);
                    displayBedsheetPrice(bedsheetCount * 3);
                    displayOtherPrice(otherCount * 3);
                    iron = 1;
                    wash = 0;
                    do_both = 0;
                }
                break;
            case R.id.washRB:
                if (checked) {

                    displayTopPrice(topCount * 100);
                    displayLowerPrice(lowerCount * 50);
                    displayBedsheetPrice(bedsheetCount * 40);
                    displayOtherPrice(otherCount * 50);
                    iron = 0;
                    wash = 1;
                    do_both = 0;
                }
                break;
            case R.id.bothRB:
                if (checked) {

                    displayTopPrice(topCount * 38);
                    displayLowerPrice(lowerCount * 30);
                    displayBedsheetPrice(bedsheetCount * 45);
                    displayOtherPrice(otherCount * 40);
                    iron = 0;
                    wash = 0;
                    do_both = 1;
                }
                break;
        }
    }

    public void updatePrice(String clothType, int count) {
        int selectedId = radioService.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        radiobtn = (RadioButton) findViewById(selectedId);
        if (radiobtn.getText().equals("Wash")) {
            if (clothType == "top") {

                displayTopPrice(count * 5);
            } else if (clothType == "Trousers") {
                displayLowerPrice(count * 10);
            } else if (clothType == "bedsheet") {
                displayBedsheetPrice(count * 12);
            } else if (clothType == "other") {
                displayOtherPrice(count * 8);
            }
        } else if (radiobtn.getText().equals("Iron")) {
            if (clothType == "top") {

                displayTopPrice(count * 3);
            } else if (clothType == "Trousers") {
                displayLowerPrice(count * 3);
            } else if (clothType == "bedsheet") {
                displayBedsheetPrice(count * 3);
            } else if (clothType == "other") {
                displayOtherPrice(count * 3);
            }
        } else if (radiobtn.getText().equals("Wash and Iron")) {

            if (clothType == "top") {

                displayTopPrice(count * 8);
            } else if (clothType == "Trousers") {
                displayLowerPrice(count * 13);
            } else if (clothType == "bedsheet") {
                displayBedsheetPrice(count * 15);
            } else if (clothType == "other") {
                displayOtherPrice(count * 11);
            }
        }
    }

    private void displayTop(int number) {

        topQty.setText("" + number);
    }

    public void topInc(View view) {
        if (topCount == 12) {
            topCount = 12;
            Toast.makeText(getApplicationContext(), "Can't be more than 12", Toast.LENGTH_SHORT).show();
        } else {
            topCount = topCount + 1;
            displayTop(topCount);

        }
        updatePrice("top", topCount);
    }

    public void topDec(View view) {
        if (topCount == 0) {
            topCount = 0;
            Toast.makeText(getApplicationContext(), "Can't be less than 0", Toast.LENGTH_SHORT).show();
        } else {
            topCount = topCount - 1;
            displayTop(topCount);
        }
        updatePrice("top", topCount);
    }

    public void lowerInc(View view) {
        if (lowerCount == 12) {
            lowerCount = 12;
            Toast.makeText(getApplicationContext(), "Can't be more than 12", Toast.LENGTH_SHORT).show();
        } else {
            lowerCount = lowerCount + 1;
            displayLower(lowerCount);
        }
        updatePrice("Trouser", lowerCount);
    }

    public void lowerDec(View view) {
        if (lowerCount == 0) {
            lowerCount = 0;
            Toast.makeText(getApplicationContext(), "Can't be less than 0", Toast.LENGTH_SHORT).show();
        } else {
            lowerCount = lowerCount - 1;
            displayLower(lowerCount);
        }
        updatePrice("trouser", lowerCount);
    }


    public void bedsheetInc(View view) {
        if (bedsheetCount == 12) {
            bedsheetCount = 12;
            Toast.makeText(getApplicationContext(), "Can't be more than 12", Toast.LENGTH_SHORT).show();
        } else {
            bedsheetCount = bedsheetCount + 1;
            displayBedsheet(bedsheetCount);
        }
        updatePrice("bedsheet", bedsheetCount);
    }

    public void bedsheetDec(View view) {
        if (bedsheetCount == 0) {
            bedsheetCount = 0;
            Toast.makeText(getApplicationContext(), "Can't be less than 0", Toast.LENGTH_SHORT).show();
        } else {
            bedsheetCount = bedsheetCount - 1;
            displayBedsheet(bedsheetCount);
        }
        updatePrice("bedsheet", bedsheetCount);
    }

    public void otherInc(View view) {
        if (otherCount == 12) {
            otherCount = 12;
            Toast.makeText(getApplicationContext(), "Can't be more than 12", Toast.LENGTH_SHORT).show();
        } else {
            otherCount = otherCount + 1;
            displayOther(otherCount);
        }
        updatePrice("other", otherCount);
    }

    public void otherDec(View view) {
        if (otherCount == 0) {
            otherCount = 0;
            Toast.makeText(getApplicationContext(), "Can't be less than 0", Toast.LENGTH_SHORT).show();
        } else {
            otherCount = otherCount - 1;
            displayOther(otherCount);
        }
        updatePrice("other", otherCount);
    }

    private void displayLower(int number) {
        lowerQty.setText("" + number);
    }

    private void displayBedsheet(int number) {
        bedsheetQty.setText("" + number);
    }

    private void displayOther(int number) {
        otherQty.setText("" + number);
    }

    private void displayTopPrice(int number) {
        topPrice.setText("" + number);
    }

    private void displayLowerPrice(int number) {

        lowerPrice.setText("" + number);
    }

    private void displayBedsheetPrice(int number) {

        bedsheetPrice.setText("" + number);
    }

    private void displayOtherPrice(int number) {

        otherPrice.setText("" + number);
    }
}