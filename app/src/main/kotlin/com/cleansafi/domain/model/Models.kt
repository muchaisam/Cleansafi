package com.cleansafi.domain.model

data class User(
    val userId: String,
    val email: String,
    val name: String,
    val phoneNumber: String
)

data class CartItem(
    val itemType: LaundryItemType,
    val quantity: Int,
    val serviceType: ServiceType,
    val pricePerItem: Int
)

data class Order(
    val orderId: Long = 0,
    val userId: String,
    val items: List<OrderItem>,
    val totalPrice: Int,
    val status: OrderStatus,
    val scheduledDate: Long,
    val scheduledTime: String,
    val createdAt: Long
)

data class OrderItem(
    val itemId: Long = 0,
    val orderId: Long = 0,
    val itemType: LaundryItemType,
    val quantity: Int,
    val serviceType: ServiceType,
    val pricePerItem: Int
)
