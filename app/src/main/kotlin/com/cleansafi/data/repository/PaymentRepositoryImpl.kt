package com.cleansafi.data.repository

import com.cleansafi.data.local.dao.PaymentDao
import com.cleansafi.data.local.entity.PaymentEntity
import com.cleansafi.domain.model.Payment
import com.cleansafi.domain.model.PaymentStatus
import com.cleansafi.domain.repository.PaymentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepositoryImpl @Inject constructor(
    private val paymentDao: PaymentDao
) : PaymentRepository {
    
    override suspend fun initiatePayment(
        orderId: Long,
        amount: Int,
        phoneNumber: String
    ): Result<Payment> {
        return try {
            val paymentId = "PAY-${UUID.randomUUID()}"
            val payment = PaymentEntity(
                paymentId = paymentId,
                orderId = orderId,
                amount = amount,
                phoneNumber = phoneNumber,
                status = PaymentStatus.PENDING.name,
                createdAt = System.currentTimeMillis()
            )
            
            paymentDao.insertPayment(payment)
            Result.success(payment.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getPaymentByOrderId(orderId: Long): Payment? {
        return paymentDao.getPaymentByOrderId(orderId)?.toDomain()
    }
    
    override suspend fun updatePaymentStatus(
        paymentId: String,
        status: PaymentStatus,
        transactionId: String?,
        failureReason: String?
    ) {
        val payment = paymentDao.getPaymentById(paymentId) ?: return
        
        val updatedPayment = payment.copy(
            status = status.name,
            transactionId = transactionId,
            failureReason = failureReason,
            completedAt = if (status in listOf(PaymentStatus.SUCCESS, PaymentStatus.FAILED))
                System.currentTimeMillis() else null
        )
        
        paymentDao.updatePayment(updatedPayment)
    }
    
    override fun observePayment(orderId: Long): Flow<Payment?> {
        return paymentDao.observePaymentByOrderId(orderId).map { it?.toDomain() }
    }
    
    override suspend fun simulatePaymentSuccess(paymentId: String, transactionId: String) {
        updatePaymentStatus(
            paymentId = paymentId,
            status = PaymentStatus.SUCCESS,
            transactionId = transactionId
        )
    }
    
    override suspend fun simulatePaymentFailure(paymentId: String, reason: String) {
        updatePaymentStatus(
            paymentId = paymentId,
            status = PaymentStatus.FAILED,
            failureReason = reason
        )
    }
    
    private fun PaymentEntity.toDomain(): Payment {
        return Payment(
            paymentId = paymentId,
            orderId = orderId,
            amount = amount,
            phoneNumber = phoneNumber,
            status = PaymentStatus.valueOf(status),
            transactionId = transactionId,
            failureReason = failureReason,
            createdAt = createdAt,
            completedAt = completedAt
        )
    }
}
