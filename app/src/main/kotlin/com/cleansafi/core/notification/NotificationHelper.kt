package com.cleansafi.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.cleansafi.MainActivity
import com.cleansafi.domain.model.OrderStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val CHANNEL_ID = "cleansafi_orders"
        private const val CHANNEL_NAME = "Order Updates"
        private const val CHANNEL_DESCRIPTION = "Notifications about your laundry orders"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendOrderStatusNotification(orderId: Long, status: OrderStatus) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("order_id", orderId)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            orderId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val (title, message, icon) = getNotificationContent(status)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(orderId.toInt(), notification)
        } catch (e: SecurityException) {
            // Handle case where notification permission is not granted
        }
    }

    private fun getNotificationContent(status: OrderStatus): Triple<String, String, Int> {
        return when (status) {
            OrderStatus.PENDING -> Triple(
                "Order Placed! ⏳",
                "Your order is awaiting payment confirmation",
                android.R.drawable.ic_dialog_info
            )

            OrderStatus.CONFIRMED -> Triple(
                "Order Confirmed! ✅",
                "Your laundry order has been confirmed. We'll pick it up soon!",
                android.R.drawable.ic_dialog_info
            )

            OrderStatus.PICKED_UP -> Triple(
                "Laundry Picked Up! 🚚",
                "Your items have been picked up and are on their way to our facility",
                android.R.drawable.ic_dialog_info
            )

            OrderStatus.PROCESSING -> Triple(
                "Cleaning in Progress! 🧺",
                "Your laundry is being professionally cleaned and cared for",
                android.R.drawable.ic_dialog_info
            )

            OrderStatus.READY -> Triple(
                "Order Ready! ✨",
                "Your freshly cleaned laundry is ready for delivery",
                android.R.drawable.ic_dialog_info
            )

            OrderStatus.OUT_FOR_DELIVERY -> Triple(
                "Out for Delivery! 🚗",
                "Your order is on its way to you. Expect delivery soon!",
                android.R.drawable.ic_dialog_info
            )

            OrderStatus.DELIVERED -> Triple(
                "Delivered! 📦",
                "Your laundry has been delivered. Thank you for choosing CleanSafi!",
                android.R.drawable.ic_dialog_info
            )

            OrderStatus.COMPLETED -> Triple(
                "Order Complete! 🎉",
                "We hope you're satisfied with our service. Order again anytime!",
                android.R.drawable.ic_dialog_info
            )

            OrderStatus.PAYMENT_FAILED -> Triple(
                "Payment Failed ❌",
                "Your payment could not be processed. Please try again.",
                android.R.drawable.ic_dialog_alert
            )

            OrderStatus.CANCELLED -> Triple(
                "Order Cancelled 🚫",
                "Your order has been cancelled.",
                android.R.drawable.ic_dialog_alert
            )
        }
    }
}
