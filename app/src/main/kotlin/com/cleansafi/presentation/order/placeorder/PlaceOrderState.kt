package com.cleansafi.presentation.order.placeorder

import com.cleansafi.core.error.AppError
import com.cleansafi.domain.model.LaundryItemType
import com.cleansafi.domain.model.ServiceType

data class PlaceOrderState(
    val serviceType: ServiceType = ServiceType.WASH_AND_IRON,
    val itemQuantities: Map<LaundryItemType, Int> = emptyMap(),
    val selectedDate: Long? = null,
    val selectedTime: String? = null,
    val totalPrice: Int = 0,
    val isLoading: Boolean = false,
    val error: AppError? = null,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false
) {
    val hasItems: Boolean
        get() = itemQuantities.values.any { it > 0 }

    val totalItems: Int
        get() = itemQuantities.values.sum()
}
