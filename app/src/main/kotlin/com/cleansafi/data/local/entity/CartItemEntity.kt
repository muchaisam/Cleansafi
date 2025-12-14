package com.cleansafi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey
    val itemType: String,
    val quantity: Int,
    val serviceType: String,
    val pricePerItem: Int
)
