package com.example.cleansafi.mpesa.utils;

public class Pair<X, Y> {
    public X code;
    public Y message;
    public Pair(X x, Y y){
        this.code = x;
        this.message = y;
    }
}