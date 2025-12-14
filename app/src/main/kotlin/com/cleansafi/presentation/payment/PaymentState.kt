package com.cleansafi.presentation.payment

import com.cleansafi.domain.model.Payment
import com.cleansafi.domain.model.PaymentStatus

data class PaymentState(
    val orderId: Long = 0,
    val amount: Int = 0,
    val phoneNumber: String = "",
    val payment: Payment? = null,
    val paymentStatus: PaymentStatus = PaymentStatus.PENDING,
    val isProcessing: Boolean = false,
    val countdown: Int = 60,
    val showDemoControls: Boolean = true, // For portfolio demonstration
    val error: String? = null,
    val isSuccess: Boolean = false
) {
    val formattedPhoneNumber: String
        get() = if (phoneNumber.isNotEmpty()) {
            phoneNumber.replaceFirst("^254".toRegex(), "+254 ")
        } else ""
}
