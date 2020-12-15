package com.example.cleansafi.mpesa.interfaces;

import com.example.cleansafi.mpesa.Mpesa;
import com.example.cleansafi.mpesa.utils.Pair;



public interface AuthListener {
    public void onAuthError(Pair<Integer, String> result);
    public void onAuthSuccess();
}
