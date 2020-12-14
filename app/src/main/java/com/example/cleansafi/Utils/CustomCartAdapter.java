package com.example.cleansafi.Utils;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleansafi.CheckOutActivity;
import com.example.cleansafi.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.cleansafi.Utils.DBHelper.TABLE_NAME;
import static com.example.cleansafi.Utils.DBHelper.TEMP_TABLE_NAME;


public class CustomCartAdapter extends RecyclerView.Adapter<CustomCartAdapter.CartHolder> {
    Context context;
    List<DBModel> dbList = new ArrayList<>();
    DBHelper dbHelper;
    OnItemClick mCallback;
    Spinner topQty;
    Spinner lowerQty;
    Spinner bedsheetQty;
    Spinner otherQty;
    int top = 0;
    int jeans = 0;
    int bedsheets = 0;
    int towels = 0;

    public CustomCartAdapter(Context context, List<DBModel> dbList, OnItemClick mCallback) {
        this.context = context;
        this.dbList = dbList;
        this.mCallback = mCallback;
    }

    @NonNull
    @Override
    public CustomCartAdapter.CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_listview, parent, false);
        dbHelper = new DBHelper(context);


        return new CartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomCartAdapter.CartHolder holder, final int position) {

        holder.TOPclothes.setText(String.valueOf(dbList.get(position).getTop_clothes()));
        holder.JeansLower.setText(String.valueOf(dbList.get(position).getJeans_lower()));
        holder.Bedsheets.setText(String.valueOf(dbList.get(position).getBedsheets()));
        holder.Towels.setText(String.valueOf(dbList.get(position).getTowels()));
        if (dbList.get(position).getDoboth() == 1) {
            holder.iron.setText("Yes");
            holder.wash.setText("Yes");
        } else {
            if (dbList.get(position).getWash_only() == 0) {
                holder.wash.setText("No");
            } else {
                holder.wash.setText("Yes");

            }
            if (dbList.get(position).getIron_only() == 0) {

                holder.iron.setText("No");
            } else {
                holder.iron.setText("Yes");
            }
        }


        holder.cart_price.setText("Ksh " + dbList.get(position).getFinal_price());
        holder.remove_from_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(position);
            }
        });
        holder.edit_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: dbList" + dbList.get(position).getId());
                updateCart(position);
            }
        });
    }

    void removeItem(final int position) {
        final Dialog removeDialog = new Dialog(context);
        removeDialog.setContentView(R.layout.delete_dialog);
        removeDialog.show();
        Button remove_item_btn = removeDialog.findViewById(R.id.yes_remove);
        Button cancel_remove_btn = removeDialog.findViewById(R.id.no_remove);
        remove_item_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.deleteEntry(String.valueOf(dbList.get(position).getId()));
                dbHelper.deleteTemp(String.valueOf(dbList.get(position).getId()));
                dbList.remove(position);
                notifyDataSetChanged();
                removeDialog.dismiss();
                mCallback.onClick(dbList, 0);

            }
        });
        cancel_remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeDialog.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return dbList.size();
    }

    class CartHolder extends RecyclerView.ViewHolder {

        TextView cart_price, TOPclothes, JeansLower, Bedsheets, Towels, wash, iron, edit_cart;
        ImageView remove_from_cart;

        public CartHolder(@NonNull View itemView) {
            super(itemView);
            cart_price = itemView.findViewById(R.id.cart_price);
            TOPclothes = itemView.findViewById(R.id.tshirtsqty);
            JeansLower = itemView.findViewById(R.id.jeansqty);
            Bedsheets = itemView.findViewById(R.id.bedsheetsqty);
            Towels = itemView.findViewById(R.id.towelqty);
            wash = itemView.findViewById(R.id.washqty);
            iron = itemView.findViewById(R.id.ironqty);
            edit_cart = itemView.findViewById(R.id.edit_cart_items);
            remove_from_cart = itemView.findViewById(R.id.delete_item);

        }
    }

    void updateCart(final int position) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.update_order_dialog);
        topQty = dialog.findViewById(R.id.topQty);
        lowerQty = dialog.findViewById(R.id.lowerQty);
        bedsheetQty = dialog.findViewById(R.id.bedsheetQty);
        otherQty = dialog.findViewById(R.id.otherQty);
        dialog.show();
        Button update_bucket = dialog.findViewById(R.id.update_bucket);

        update_bucket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (dbList.get(position).getWash_only() == 1) {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    top = Integer.parseInt(String.valueOf(topQty.getSelectedItem()));
                    jeans = Integer.parseInt(String.valueOf(lowerQty.getSelectedItem()));
                    bedsheets = Integer.parseInt(String.valueOf(bedsheetQty.getSelectedItem()));
                    towels = Integer.parseInt(String.valueOf(otherQty.getSelectedItem()));
                    cv.put("top_clothes", top * 50); //These Fields should be your String values of actual column names
                    cv.put("jeans_lower", jeans * 50);
                    cv.put("bedsheets", bedsheets * 62);
                    cv.put("towels", towels * 80);
                    cv.put("wash_only", 100);
                    cv.put("final_price", top * 50 + jeans * 50 + bedsheets * 62 + towels * 80);

                    db.update(TEMP_TABLE_NAME, cv, "column_id= " + dbList.get(position).getId(), null);
                    db.update(TABLE_NAME, cv, "column_id= " + dbList.get(position).getId(), null);
                    mCallback.onClick(dbList, top * 50 + jeans * 50 + bedsheets * 62 + towels *80 );

                } else if (dbList.get(position).getIron_only() == 1) {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    top = Integer.parseInt(String.valueOf(topQty.getSelectedItem()));
                    jeans = Integer.parseInt(String.valueOf(lowerQty.getSelectedItem()));
                    bedsheets = Integer.parseInt(String.valueOf(bedsheetQty.getSelectedItem()));
                    towels = Integer.parseInt(String.valueOf(otherQty.getSelectedItem()));
                    cv.put("top_clothes", top * 30); //These Fields should be your String values of actual column names
                    cv.put("jeans", jeans * 30);
                    cv.put("bedsheets", bedsheets * 30);
                    cv.put("towels", towels * 35);
                    cv.put("iron_only", 70);
                    cv.put("final_price", top * 30 + jeans * 30 + bedsheets * 30 + towels * 35);
                    db.update(TEMP_TABLE_NAME, cv, "column_id= " + dbList.get(position).getId(), null);
                    db.update(TABLE_NAME, cv, "column_id= " + dbList.get(position).getId(), null);
                    mCallback.onClick(dbList, top * 30 + jeans * 30 + bedsheets * 30 + towels * 35);

                } else {
                    if (dbList.get(position).getDoboth() == 1) {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues cv = new ContentValues();
                        String topp = String.valueOf(topQty.getSelectedItem());
                        top = Integer.parseInt(String.valueOf(topQty.getSelectedItem()));
                        jeans = Integer.parseInt(String.valueOf(lowerQty.getSelectedItem()));
                        bedsheets = Integer.parseInt(String.valueOf(bedsheetQty.getSelectedItem()));
                        towels = Integer.parseInt(String.valueOf(otherQty.getSelectedItem()));
                        cv.put("top_clothes", top * 80); //These Fields should be your String values of actual column names
                        cv.put("jeans_lower", jeans * 100);
                        cv.put("bedsheets", bedsheets * 150);
                        cv.put("towels", towels * 110);
                        cv.put("doboth", 1);
                        cv.put("final_price", top * 80 + jeans * 100 + bedsheets * 150 + towels * 110);

                        db.update(TABLE_NAME, cv, "column_id= " + dbList.get(position).getId(), null);
                        db.update(TEMP_TABLE_NAME, cv, "column_id= " + dbList.get(position).getId(), null);
                        mCallback.onClick(dbList, top * 80 + jeans * 100 + bedsheets * 150 + towels * 110);

                    }
                }
                notifyDataSetChanged();
                dialog.dismiss();
                context.startActivity(new Intent(context, CheckOutActivity.class));

            }
        });

    }

}
