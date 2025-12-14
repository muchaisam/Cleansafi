package com.cleansafi.core.simulation

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cleansafi.core.notification.NotificationHelper
import com.cleansafi.domain.model.OrderStatus
import com.cleansafi.domain.repository.OrderRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay

@HiltWorker
class OrderProgressionWorkerFactory @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val orderRepository: OrderRepository,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        val orderId = inputData.getLong("orderId", 0L)
        if (orderId == 0L) return Result.failure()
        
        try {
            // Simulate realistic order progression timeline
            progressOrder(orderId, OrderStatus.PICKED_UP, delayMinutes = 0) // Immediately after payment
            progressOrder(orderId, OrderStatus.PROCESSING, delayMinutes = 2)
            progressOrder(orderId, OrderStatus.READY, delayMinutes = 3)
            progressOrder(orderId, OrderStatus.OUT_FOR_DELIVERY, delayMinutes = 1)
            progressOrder(orderId, OrderStatus.DELIVERED, delayMinutes = 2)
            progressOrder(orderId, OrderStatus.COMPLETED, delayMinutes = 0)
            
            return Result.success()
        } catch (e: Exception) {
            return Result.retry()
        }
    }
    
    private suspend fun progressOrder(orderId: Long, status: OrderStatus, delayMinutes: Long) {
        // Check if demo mode is enabled for faster progression
        val demoModeManager = com.cleansafi.core.util.DemoModeManager(applicationContext)
        val delaySeconds = demoModeManager.getProgressionDelaySeconds(delayMinutes)
        
        delay(delaySeconds * 1000) // Convert seconds to milliseconds
        orderRepository.updateOrderStatus(orderId, status)
        
        // Send notification
        notificationHelper.sendOrderStatusNotification(orderId, status)
    }
}
