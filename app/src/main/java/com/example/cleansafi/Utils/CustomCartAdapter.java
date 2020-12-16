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


}
