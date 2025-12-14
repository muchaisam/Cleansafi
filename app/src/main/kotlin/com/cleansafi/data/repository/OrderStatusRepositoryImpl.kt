package com.cleansafi.data.repository

import com.cleansafi.data.local.dao.OrderDao
import com.cleansafi.data.local.dao.OrderStatusHistoryDao
import com.cleansafi.data.local.entity.OrderStatusHistoryEntity
import com.cleansafi.domain.model.OrderStatus
import com.cleansafi.domain.model.OrderStatusUpdate
import com.cleansafi.domain.repository.OrderStatusRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderStatusRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao,
    private val orderStatusHistoryDao: OrderStatusHistoryDao
) : OrderStatusRepository {
    
    override suspend fun updateOrderStatus(orderId: Long, status: OrderStatus, note: String?) {
        // Update order status
        val orderWithItems = orderDao.getOrderById(orderId) ?: return
        val updatedOrder = orderWithItems.order.copy(status = status.name)
        orderDao.updateOrder(updatedOrder)
        
        // Record status change in history
        val historyEntry = OrderStatusHistoryEntity(
            orderId = orderId,
            status = status.name,
            timestamp = System.currentTimeMillis(),
            note = note
        )
        orderStatusHistoryDao.insertStatusUpdate(historyEntry)
    }
    
    override suspend fun getOrderStatusHistory(orderId: Long): List<OrderStatusUpdate> {
        return orderStatusHistoryDao.getOrderHistory(orderId).map { it.toDomain() }
    }
    
    override fun observeOrderStatusHistory(orderId: Long): Flow<List<OrderStatusUpdate>> {
        return orderStatusHistoryDao.observeOrderHistory(orderId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun progressOrderToNextStatus(orderId: Long) {
        val orderWithItems = orderDao.getOrderById(orderId) ?: return
        val currentStatus = OrderStatus.valueOf(orderWithItems.order.status)
        val nextStatus = currentStatus.nextStatus() ?: return
        
        updateOrderStatus(
            orderId = orderId,
            status = nextStatus,
            note = "Status automatically progressed"
        )
    }
    
    private fun OrderStatusHistoryEntity.toDomain(): OrderStatusUpdate {
        return OrderStatusUpdate(
            orderId = orderId,
            status = OrderStatus.valueOf(status),
            timestamp = timestamp,
            note = note
        )
    }
}
