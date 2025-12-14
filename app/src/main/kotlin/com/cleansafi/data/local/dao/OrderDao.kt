package com.cleansafi.data.local.dao

import androidx.room.*
import com.cleansafi.data.local.entity.OrderEntity
import com.cleansafi.data.local.entity.OrderItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Transaction
    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY createdAt DESC")
    fun observeUserOrders(userId: String): Flow<List<OrderWithItems>>
    
    @Transaction
    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getUserOrders(userId: String): List<OrderWithItems>
    
    @Transaction
    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    suspend fun getOrderById(orderId: Long): OrderWithItems?
    
    @Insert
    suspend fun insertOrder(order: OrderEntity): Long
    
    @Insert
    suspend fun insertOrderItems(items: List<OrderItemEntity>)
    
    @Transaction
    suspend fun insertOrderWithItems(order: OrderEntity, items: List<OrderItemEntity>): Long {
        val orderId = insertOrder(order)
        val itemsWithOrderId = items.map { it.copy(orderId = orderId) }
        insertOrderItems(itemsWithOrderId)
        return orderId
    }
    
    @Update
    suspend fun updateOrder(order: OrderEntity)
    
    @Query("SELECT SUM(totalPrice) FROM orders WHERE userId = :userId AND status = 'COMPLETED'")
    fun observeTotalSpent(userId: String): Flow<Int?>
}

data class OrderWithItems(
    @Embedded val order: OrderEntity,
    @Relation(
        parentColumn = "orderId",
        entityColumn = "orderId"
    )
    val items: List<OrderItemEntity>
)
