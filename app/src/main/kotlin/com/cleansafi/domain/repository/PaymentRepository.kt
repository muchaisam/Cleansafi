package com.cleansafi.domain.repository

import com.cleansafi.domain.model.Payment
import com.cleansafi.domain.model.PaymentStatus
import kotlinx.coroutines.flow.Flow

interface PaymentRepository {
    suspend fun initiatePayment(orderId: Long, amount: Int, phoneNumber: String): Result<Payment>
    suspend fun getPaymentByOrderId(orderId: Long): Payment?
    suspend fun updatePaymentStatus(paymentId: String, status: PaymentStatus, transactionId: String? = null, failureReason: String? = null)
    fun observePayment(orderId: Long): Flow<Payment?>
    suspend fun simulatePaymentSuccess(paymentId: String, transactionId: String)
    suspend fun simulatePaymentFailure(paymentId: String, reason: String)
}
