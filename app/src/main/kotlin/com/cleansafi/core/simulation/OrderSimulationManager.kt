package com.cleansafi.core.simulation

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.cleansafi.core.notification.NotificationHelper
import com.cleansafi.domain.model.OrderStatus
import com.cleansafi.domain.repository.OrderRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Background service that simulates real-world order progression
 * for portfolio demonstration purposes.
 */
@Singleton
class OrderSimulationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun scheduleOrderProgression(orderId: Long) {
        val workRequest = OneTimeWorkRequestBuilder<OrderProgressionWorkerFactory>()
            .setInputData(
                workDataOf("orderId" to orderId)
            )
            .setInitialDelay(30, TimeUnit.SECONDS) // Start after 30 seconds
            .addTag("order_$orderId")
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    fun cancelOrderProgression(orderId: Long) {
        WorkManager.getInstance(context).cancelAllWorkByTag("order_$orderId")
    }
}

class OrderProgressionWorker(
    context: Context,
    params: WorkerParameters,
    private val orderRepository: OrderRepository,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val orderId = inputData.getLong("orderId", 0L)
        if (orderId == 0L) return Result.failure()

        try {
            // Simulate realistic order progression timeline
            progressOrder(
                orderId,
                OrderStatus.PICKED_UP,
                delayMinutes = 0
            ) // Immediately after payment
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
        delay(delayMinutes * 60 * 1000) // Convert minutes to milliseconds
        orderRepository.updateOrderStatus(orderId, status)

        // Send notification
        notificationHelper.sendOrderStatusNotification(orderId, status)
    }
}
