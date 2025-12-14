package com.cleansafi.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "payments",
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
data class PaymentEntity(
    @PrimaryKey
    val paymentId: String,
    val orderId: Long,
    val amount: Int,
    val phoneNumber: String,
    val status: String,
    val transactionId: String? = null,
    val failureReason: String? = null,
    val createdAt: Long,
    val completedAt: Long? = null
)

@Entity(tableName = "order_status_history")
data class OrderStatusHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderId: Long,
    val status: String,
    val timestamp: Long,
    val note: String? = null
)
