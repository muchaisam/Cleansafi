package com.cleansafi.presentation.order.placeorder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleansafi.core.error.toAppError
import com.cleansafi.core.util.PreferencesManager
import com.cleansafi.domain.model.CartItem
import com.cleansafi.domain.model.LaundryItemType
import com.cleansafi.domain.model.ServiceType
import com.cleansafi.domain.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PlaceOrderViewModel
@Inject
constructor(
        private val cartRepository: CartRepository,
        private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _state = MutableStateFlow(PlaceOrderState())
    val state = _state.asStateFlow()

    init {
        loadExistingCart()
    }

    private fun loadExistingCart() {
        viewModelScope.launch {
            try {
                val existingItems = cartRepository.getCartItems()
                if (existingItems.isNotEmpty()) {
                    val quantities = existingItems.associate { it.itemType to it.quantity }
                    val serviceType =
                            existingItems.firstOrNull()?.serviceType ?: ServiceType.WASH_AND_IRON

                    _state.update {
                        it.copy(
                                itemQuantities = quantities,
                                serviceType = serviceType,
                                error = null
                        )
                    }
                    recalculatePrice()
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                            error =
                                    com.cleansafi.core.error.AppError.DatabaseError(
                                            e.message ?: "Error loading cart"
                                    )
                    )
                }
            }
        }
    }

    fun retry() {
        _state.update { it.copy(error = null) }
        loadExistingCart()
    }

    fun onServiceTypeChange(serviceType: ServiceType) {
        _state.update { it.copy(serviceType = serviceType) }
        recalculatePrice()
    }

    fun onQuantityChange(itemType: LaundryItemType, quantity: Int) {
        val newQuantities = _state.value.itemQuantities.toMutableMap()
        if (quantity > 0) {
            newQuantities[itemType] = quantity
        } else {
            newQuantities.remove(itemType)
        }

        _state.update { it.copy(itemQuantities = newQuantities) }
        recalculatePrice()
    }

    fun incrementQuantity(itemType: LaundryItemType) {
        val currentQuantity = _state.value.itemQuantities[itemType] ?: 0
        onQuantityChange(itemType, currentQuantity + 1)
    }

    fun decrementQuantity(itemType: LaundryItemType) {
        val currentQuantity = _state.value.itemQuantities[itemType] ?: 0
        if (currentQuantity > 0) {
            onQuantityChange(itemType, currentQuantity - 1)
        }
    }

    fun onDateSelected(dateMillis: Long) {
        _state.update { it.copy(selectedDate = dateMillis, showDatePicker = false) }
    }

    fun onTimeSelected(hour: Int, minute: Int) {
        val timeString = String.format("%02d:%02d", hour, minute)
        _state.update { it.copy(selectedTime = timeString, showTimePicker = false) }
    }

    fun showDatePicker() {
        _state.update { it.copy(showDatePicker = true) }
    }

    fun hideDatePicker() {
        _state.update { it.copy(showDatePicker = false) }
    }

    fun showTimePicker() {
        _state.update { it.copy(showTimePicker = true) }
    }

    fun hideTimePicker() {
        _state.update { it.copy(showTimePicker = false) }
    }

    fun addToCart() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                // Clear existing cart
                cartRepository.clearCart()

                // Add all items to cart
                _state.value.itemQuantities.forEach { (itemType, quantity) ->
                    val pricePerItem =
                            PricingCalculator.calculateItemPrice(itemType, _state.value.serviceType)

                    cartRepository.addToCart(
                            CartItem(
                                    itemType = itemType,
                                    quantity = quantity,
                                    serviceType = _state.value.serviceType,
                                    pricePerItem = pricePerItem
                            )
                    )
                }

                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.toAppError()) }
            }
        }
    }

    private fun recalculatePrice() {
        val totalPrice =
                PricingCalculator.calculateTotalPrice(
                        _state.value.itemQuantities,
                        _state.value.serviceType
                )
        _state.update { it.copy(totalPrice = totalPrice) }
    }
}
