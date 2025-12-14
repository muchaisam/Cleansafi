package com.cleansafi.domain.repository

import com.cleansafi.domain.model.Order
import com.cleansafi.domain.model.OrderStatus
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun observeUserOrders(userId: String): Flow<List<Order>>
    suspend fun getUserOrders(userId: String): List<Order>
    suspend fun getOrderById(orderId: Long): Order?
    suspend fun createOrder(order: Order): Result<Long>
    suspend fun updateOrderStatus(orderId: Long, status: OrderStatus)
    fun observeTotalSpent(userId: String): Flow<Int>
}
