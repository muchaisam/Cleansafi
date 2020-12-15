package com.example.cleansafi.mpesa.interfaces;

import com.example.cleansafi.mpesa.utils.Pair;



public interface MpesaListener {
    public void onMpesaError(Pair<Integer, String> result);
    public void onMpesaSuccess(String MerchantRequestID, String CheckoutRequestID, String CustomerMessage);
}
