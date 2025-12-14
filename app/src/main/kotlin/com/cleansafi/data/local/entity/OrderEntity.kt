package com.cleansafi.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "orders",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val orderId: Long = 0,
    val userId: String,
    val totalPrice: Int,
    val status: String,
    val scheduledDate: Long,
    val scheduledTime: String,
    val createdAt: Long
)
