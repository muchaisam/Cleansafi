package com.cleansafi.domain.model

data class Payment(
    val paymentId: String,
    val orderId: Long,
    val amount: Int,
    val phoneNumber: String,
    val status: PaymentStatus,
    val transactionId: String? = null,
    val failureReason: String? = null,
    val createdAt: Long,
    val completedAt: Long? = null
)

data class OrderStatusUpdate(
    val orderId: Long,
    val status: OrderStatus,
    val timestamp: Long,
    val note: String? = null
)
