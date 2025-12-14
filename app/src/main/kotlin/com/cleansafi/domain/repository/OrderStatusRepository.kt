package com.cleansafi.domain.repository

import com.cleansafi.domain.model.OrderStatus
import com.cleansafi.domain.model.OrderStatusUpdate
import kotlinx.coroutines.flow.Flow

interface OrderStatusRepository {
    suspend fun updateOrderStatus(orderId: Long, status: OrderStatus, note: String? = null)
    suspend fun getOrderStatusHistory(orderId: Long): List<OrderStatusUpdate>
    fun observeOrderStatusHistory(orderId: Long): Flow<List<OrderStatusUpdate>>
    suspend fun progressOrderToNextStatus(orderId: Long)
}
