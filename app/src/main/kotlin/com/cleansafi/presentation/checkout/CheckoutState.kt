package com.cleansafi.presentation.checkout

import com.cleansafi.domain.model.CartItem

data class CheckoutState(
    val items: List<CartItem> = emptyList(),
    val totalPrice: Int = 0,
    val selectedDate: Long? = null,
    val selectedTime: String? = null,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val isProcessing: Boolean = false,
    val error: String? = null,
    val orderPlacedSuccessfully: Boolean = false,
    val createdOrderId: Long? = null
) {
    val canProceed: Boolean
        get() = items.isNotEmpty() && selectedDate != null && selectedTime != null

    val serviceType: String
        get() = items.firstOrNull()?.serviceType?.name ?: ""
}
