package com.example.cleansafi.Utils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.cleansafi.Utils.DBModel;
import com.example.cleansafi.Utils.OrderModel;
import com.example.cleansafi.Utils.UserOrderModel;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "laundry.db";
    static final String TABLE_NAME = "laundryOrdersTable";
    static final String TEMP_TABLE_NAME = "Temp_Orders_Table";
    public static final String SIGNUP_TABLE_NAME = "SIGNUP_TABLE_LAUNDRY";
    static final String ADDRESS_TABLE_NAME = "ADDRESS_TABLE";
    static final String USER_ORDER = "USER_ORDER_TABLE";
    static final String USER_ORDER_PLACED = "USER_ORDER_TABLE_PLACED";
    static final String IRONANDWASH = "doboth";
    static final String WASH_ONLY = "wash_only";
    static final String IRON_ONLY = "iron_only";
    static final String TOP_CLOTHES = "top_clothes";
    static final String JEANS_LOWER = "jeans_lower";
    static final String BEDSHEETS = "bedsheets";
    static final String OTHERS = "towels";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + "(column_id integer primary key, top_clothes integer,jeans_lower integer,bedsheets integer," +
                "towels integer,wash_only integer,iron_only integer,doboth integer, final_price integer,date text,time text)");
        sqLiteDatabase.execSQL("create table " + TEMP_TABLE_NAME + "(column_id integer primary key, top_clothes integer,jeans_lower integer,bedsheets integer," +
                "towels integer,wash_only integer,iron_only integer,doboth integer, final_price integer,date text,time text)");
        sqLiteDatabase.execSQL("create table " + USER_ORDER_PLACED + "(column_id integer primary key, top_clothes integer,jeans_lower integer,bedsheets integer," +
                "towels integer,wash_only integer,iron_only integer,doboth integer, final_price integer,date text,time text,email_id text)");
        sqLiteDatabase.execSQL("create table " + SIGNUP_TABLE_NAME + "(user_id integer primary key, first_name text,last_name text," +
                "email_id text,phone_number integer,password text)");
        sqLiteDatabase.execSQL("create table " + ADDRESS_TABLE_NAME + "(user_id integer primary key, full_name text," +
                "email_id text,phone_number integer,address text)");
        sqLiteDatabase.execSQL("create table " + USER_ORDER + "(id integer primary key," +
                " order_email text,phone_number text,address text,column_id integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //    Creating a method to get product from history_prduct table
    public List<DBModel> getDataFromDbForHistory() {

        // Declaring a List object
        List<DBModel> modelList = new ArrayList<>();

//        String for query
        String query = "select * from " + TABLE_NAME;


        SQLiteDatabase db = this.getWritableDatabase();

//        creating cursor object to use query to get the data from table
        Cursor cursor = db.rawQuery(query, null);

//        Condition for set the data in pojo class
        if (cursor.moveToFirst()) {
            do {
//                creating DatabaseModel object to set the list using by Pojo
                DBModel model = new DBModel();
                model.setId((cursor).getInt(0));
                model.setTop_clothes((cursor).getInt(1));
                model.setJeans_lower((cursor).getInt(2));
                model.setBedsheets((cursor).getInt(3));
                model.setTowels((cursor).getInt(4));
                model.setWash_only((cursor).getInt(5));
                model.setIron_only((cursor).getInt(6));
                model.setDoboth((cursor).getInt(7));
                model.setFinal_price((cursor).getInt(8));
                model.setDate((cursor).getString(9));
                model.setTime((cursor).getString(10));
                modelList.add(model);
            } while (cursor.moveToNext());
        }


        Log.d("Cart data", modelList.toString());


        return modelList;
    }

    public List<DBModel> getDataTemp() {

        // Declaring a List object
        List<DBModel> modelList = new ArrayList<>();

//        String for query
        String query = "select * from " + TEMP_TABLE_NAME;


        SQLiteDatabase db = this.getWritableDatabase();

//        creating cursor object to use query to get the data from table
        Cursor cursor = db.rawQuery(query, null);

//        Condition for set the data in pojo class
        if (cursor.moveToFirst()) {
            do {
//                creating DatabaseModel object to set the list using by Pojo
                DBModel model = new DBModel();
                model.setId((cursor).getInt(0));
                model.setTop_clothes((cursor).getInt(1));
                model.setJeans_lower((cursor).getInt(2));
                model.setBedsheets((cursor).getInt(3));
                model.setTowels((cursor).getInt(4));
                model.setWash_only((cursor).getInt(5));
                model.setIron_only((cursor).getInt(6));
                model.setDoboth((cursor).getInt(7));
                model.setFinal_price((cursor).getInt(8));
                model.setDate((cursor).getString(9));
                model.setTime((cursor).getString(10));
                modelList.add(model);
            } while (cursor.moveToNext());
        }


        Log.d("Cart data", modelList.toString());


        return modelList;
    }

    public List<UserOrderModel> getUserOrder() {

        // Declaring a List object
        List<UserOrderModel> modelList = new ArrayList<>();

//        String for query
        String query = "select * from " + USER_ORDER_PLACED;


        SQLiteDatabase db = this.getWritableDatabase();

//        creating cursor object to use query to get the data from table
        Cursor cursor = db.rawQuery(query, null);

//        Condition for set the data in pojo class
        if (cursor.moveToFirst()) {
            do {
//                creating DatabaseModel object to set the list using by Pojo
                UserOrderModel model = new UserOrderModel();
                model.setId((cursor).getInt(0));
                model.setTop_clothes((cursor).getInt(1));
                model.setJeans_lower((cursor).getInt(2));
                model.setBedsheets((cursor).getInt(3));
                model.setTowels((cursor).getInt(4));
                model.setWash_only((cursor).getInt(5));
                model.setIron_only((cursor).getInt(6));
                model.setDoboth((cursor).getInt(7));
                model.setFinal_price((cursor).getInt(8));
                model.setDate((cursor).getString(9));
                model.setTime((cursor).getString(10));
                model.setEmail((cursor).getString(11));
                modelList.add(model);
            } while (cursor.moveToNext());
        }


        Log.d("Cart data", modelList.toString());


        return modelList;
    }

    //Delete single item from Table
    public void deleteEntry(String item_id) {
        SQLiteDatabase ourDatabase = this.getWritableDatabase();
        ourDatabase.delete(TABLE_NAME, "column_id" + " = '" + item_id + "'", null);
    }

    public void deleteTemp(String item_id) {
        SQLiteDatabase ourDatabase = this.getWritableDatabase();
        ourDatabase.delete(TEMP_TABLE_NAME, "column_id" + " = '" + item_id + "'", null);
    }

    public void deleteAddress(String item_id) {
        SQLiteDatabase ourDatabase = this.getWritableDatabase();
        ourDatabase.delete(ADDRESS_TABLE_NAME, "user_id" + " = '" + item_id + "'", null);
    }

    public boolean insertUserOrder(String email_id, int top_clothes, int jeans_lower, int bedsheets, int towels, int wash_only, int iron_only, int doboth, int final_price, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("email_id", email_id);
        contentValues.put("top_clothes", top_clothes);
        contentValues.put("jeans_lower", jeans_lower);
        contentValues.put("bedsheets", bedsheets);
        contentValues.put("towels", towels);
        contentValues.put("wash_only", wash_only);
        contentValues.put("iron_only", iron_only);
        contentValues.put("doboth", doboth);
        contentValues.put("final_price", final_price);
        contentValues.put("date", date);
        contentValues.put("time", time);
        db.insert(USER_ORDER_PLACED, null, contentValues);
        return true;
    }

    public boolean insert(int top_clothes, int jeans_lower, int bedsheets, int towels, int wash_only, int iron_only, int doboth, int final_price, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("top_clothes", top_clothes);
        contentValues.put("jeans_lower", jeans_lower);
        contentValues.put("bedsheets", bedsheets);
        contentValues.put("towels", towels);
        contentValues.put("wash_only", wash_only);
        contentValues.put("iron_only", iron_only);
        contentValues.put("doboth", doboth);
        contentValues.put("final_price", final_price);
        contentValues.put("date", date);
        contentValues.put("time", time);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertTemp(int top_clothes, int jeans_lower, int bedsheets, int towels, int wash_only, int iron_only, int doboth, int final_price, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("top_clothes", top_clothes);
        contentValues.put("jeans_lower", jeans_lower);
        contentValues.put("bedsheets", bedsheets);
        contentValues.put("towels", towels);
        contentValues.put("wash_only", wash_only);
        contentValues.put("iron_only", iron_only);
        contentValues.put("doboth", doboth);
        contentValues.put("final_price", final_price);
        contentValues.put("date", date);
        contentValues.put("time", time);
        db.insert(TEMP_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertAddress(String first_name, String email_id, String phone_number, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("full_name", first_name);
        contentValues.put("email_id", email_id);
        contentValues.put("phone_number", phone_number);
        contentValues.put("address", address);
        db.insert(ADDRESS_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertOrder(String order_email, String phone_number, String address, int column_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("order_email", order_email);
        contentValues.put("phone_number", phone_number);
        contentValues.put("address", address);
        contentValues.put("column_id", column_id);
        db.insert(USER_ORDER, null, contentValues);
        return true;
    }


    public List<OrderModel> getOrderFromDb() {

        // Declaring a List object
        List<OrderModel> modelList = new ArrayList<>();

//        String for query
        String query = "select * from " + USER_ORDER;
        SQLiteDatabase db = this.getWritableDatabase();

//        creating cursor object to use query to get the data from table
        Cursor cursor = db.rawQuery(query, null);

//        Condition for set the data in pojo class
        if (cursor.moveToFirst()) {
            do {
//              creating DatabaseModel object to set the list using by Pojo
                OrderModel model = new OrderModel();
                model.setOrder_email((cursor).getString(1));
                model.setPhone_number((cursor).getString(2));
                model.setAddress((cursor).getString(3));
                model.setColumn_id((cursor).getInt(4));
                modelList.add(model);
            } while (cursor.moveToNext());
        }


        Log.d("Cart data", modelList.toString());


        return modelList;
    }

    public boolean insertSignup(String first_name, String last_name, String email_id, String phone_number, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("first_name", first_name);
        contentValues.put("last_name", last_name);
        contentValues.put("email_id", email_id);
        contentValues.put("phone_number", phone_number);
        contentValues.put("password", password);
        db.insert(SIGNUP_TABLE_NAME, null, contentValues);
        return true;
    }

}