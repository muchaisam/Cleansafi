package com.cleansafi.data.repository

import com.cleansafi.data.local.dao.OrderDao
import com.cleansafi.data.mapper.toDomain
import com.cleansafi.data.mapper.toEntity
import com.cleansafi.domain.model.Order
import com.cleansafi.domain.model.OrderStatus
import com.cleansafi.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao
) : OrderRepository {
    
    override fun observeUserOrders(userId: String): Flow<List<Order>> {
        return orderDao.observeUserOrders(userId).map { ordersWithItems ->
            ordersWithItems.map { it.toDomain() }
        }
    }
    
    override suspend fun getUserOrders(userId: String): List<Order> {
        return orderDao.getUserOrders(userId).map { it.toDomain() }
    }
    
    override suspend fun getOrderById(orderId: Long): Order? {
        return orderDao.getOrderById(orderId)?.toDomain()
    }
    
    override suspend fun createOrder(order: Order): Result<Long> {
        return try {
            val orderId = orderDao.insertOrderWithItems(
                order = order.toEntity(),
                items = order.items.map { it.toEntity() }
            )
            Result.success(orderId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateOrderStatus(orderId: Long, status: OrderStatus) {
        val orderWithItems = orderDao.getOrderById(orderId)
            ?: throw Exception("Order not found")
        
        val updatedOrder = orderWithItems.order.copy(status = status.name)
        orderDao.updateOrder(updatedOrder)
    }
    
    override fun observeTotalSpent(userId: String): Flow<Int> {
        return orderDao.observeTotalSpent(userId).map { it ?: 0 }
    }
}
