package com.cleansafi.presentation.orders

import com.cleansafi.core.error.AppError
import com.cleansafi.domain.model.Order

data class OrdersState(
    val orders: List<Order> = emptyList(),
    val totalSpent: Int = 0,
    val isLoading: Boolean = true,
    val error: AppError? = null
)
