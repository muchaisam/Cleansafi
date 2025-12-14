package com.cleansafi.presentation.cart

import com.cleansafi.domain.model.CartItem
import com.cleansafi.domain.model.LaundryItemType

data class CartState(
    val items: List<CartItem> = emptyList(),
    val totalPrice: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val suggestedItems: List<SuggestedItem> = emptyList()
) {
    val serviceType: String
        get() = items.firstOrNull()?.serviceType?.name ?: ""
}

data class SuggestedItem(
    val itemType: LaundryItemType,
    val reason: String,
    val discountPercent: Int = 0
)
