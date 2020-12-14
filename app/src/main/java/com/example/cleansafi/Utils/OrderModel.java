package com.example.cleansafi.Utils;


public class OrderModel {
    String order_email, phone_number, address;
    int column_id;
    int top_clothes;
    int jeans_lower;
    int bedsheets;
    int towels;
    int wash_only;
    int iron_only;
    int doboth;
    int final_price;
    String date, time;

    public int getTop_clothes() {
        return top_clothes;
    }

    public void setTop_clothes(int top_clothes) {
        this.top_clothes = top_clothes;
    }

    public int getJeans_lower() {
        return jeans_lower;
    }

    public void setJeans_lower(int jeans_lower) {
        this.jeans_lower = jeans_lower;
    }

    public int getBedsheets() {
        return bedsheets;
    }

    public void setBedsheets(int bedsheets) {
        this.bedsheets = bedsheets;
    }

    public int getTowels() {
        return towels;
    }

    public void setTowels(int towels) {
        this.towels = towels;
    }

    public int getWash_only() {
        return wash_only;
    }

    public void setWash_only(int wash_only) {
        this.wash_only = wash_only;
    }

    public int getIron_only() {
        return iron_only;
    }

    public void setIron_only(int iron_only) {
        this.iron_only = iron_only;
    }

    public int getDoboth() {
        return doboth;
    }

    public void setDoboth(int doboth) {
        this.doboth = doboth;
    }

    public int getFinal_price() {
        return final_price;
    }

    public void setFinal_price(int final_price) {
        this.final_price = final_price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public int getColumn_id() {
        return column_id;
    }

    public void setColumn_id(int coluumn_id) {
        this.column_id = coluumn_id;
    }

    public String getOrder_email() {
        return order_email;
    }

    public void setOrder_email(String order_email) {
        this.order_email = order_email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}