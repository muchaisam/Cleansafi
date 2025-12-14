package com.cleansafi.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cleansafi.data.local.entity.OrderStatusHistoryEntity
import com.cleansafi.data.local.entity.PaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentEntity)

    @Query("SELECT * FROM payments WHERE orderId = :orderId")
    suspend fun getPaymentByOrderId(orderId: Long): PaymentEntity?

    @Query("SELECT * FROM payments WHERE paymentId = :paymentId")
    suspend fun getPaymentById(paymentId: String): PaymentEntity?

    @Update
    suspend fun updatePayment(payment: PaymentEntity)

    @Query("SELECT * FROM payments WHERE orderId = :orderId")
    fun observePaymentByOrderId(orderId: Long): Flow<PaymentEntity?>
}

@Dao
interface OrderStatusHistoryDao {
    @Insert
    suspend fun insertStatusUpdate(update: OrderStatusHistoryEntity)

    @Query("SELECT * FROM order_status_history WHERE orderId = :orderId ORDER BY timestamp ASC")
    suspend fun getOrderHistory(orderId: Long): List<OrderStatusHistoryEntity>

    @Query("SELECT * FROM order_status_history WHERE orderId = :orderId ORDER BY timestamp ASC")
    fun observeOrderHistory(orderId: Long): Flow<List<OrderStatusHistoryEntity>>
}
