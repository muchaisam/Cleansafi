package com.cleansafi.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["orderId"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("orderId")]
)
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true)
    val itemId: Long = 0,
    val orderId: Long,
    val itemType: String,
    val quantity: Int,
    val serviceType: String,
    val pricePerItem: Int
)
