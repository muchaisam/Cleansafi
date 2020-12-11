package com.example.cleansafi.Utils;

import com.example.cleansafi.Models.DBModel;

import java.util.List;

public interface OnItemClick {
    void onClick(List<DBModel> dbList, int i);
}
