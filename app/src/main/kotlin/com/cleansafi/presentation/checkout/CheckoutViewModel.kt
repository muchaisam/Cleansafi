package com.cleansafi.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleansafi.core.error.toAppError
import com.cleansafi.core.util.PreferencesManager
import com.cleansafi.core.validation.Validators
import com.cleansafi.domain.model.Order
import com.cleansafi.domain.model.OrderItem
import com.cleansafi.domain.model.OrderStatus
import com.cleansafi.domain.repository.CartRepository
import com.cleansafi.domain.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _state = MutableStateFlow(CheckoutState())
    val state = _state.asStateFlow()
    
    init {
        loadCartItems()
    }
    
    private fun loadCartItems() {
        viewModelScope.launch {
            try {
                val items = cartRepository.getCartItems()
                val total = cartRepository.getCartTotal()
                
                _state.update { 
                    it.copy(
                        items = items,
                        totalPrice = total,
                        error = null
                    ) 
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Error loading cart") }
            }
        }
    }
    
    fun retry() {
        _state.update { it.copy(error = null) }
        loadCartItems()
    }
    
    fun onDateSelected(dateMillis: Long) {
        _state.update { 
            it.copy(
                selectedDate = dateMillis,
                showDatePicker = false,
                error = null
            ) 
        }
    }
    
    fun onTimeSelected(hour: Int, minute: Int) {
        val timeString = String.format("%02d:%02d", hour, minute)
        _state.update { 
            it.copy(
                selectedTime = timeString,
                showTimePicker = false,
                error = null
            ) 
        }
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
    
    fun placeOrder() {
        if (!_state.value.canProceed) {
            _state.update { 
                it.copy(error = "Please select date and time for pickup") 
            }
            return
        }
        
        viewModelScope.launch {
            _state.update { it.copy(isProcessing = true, error = null) }
            
            try {
                val userId = preferencesManager.currentUserId
                    ?: throw Exception("User not logged in")
                
                val order = Order(
                    userId = userId,
                    items = _state.value.items.map { cartItem ->
                        OrderItem(
                            itemType = cartItem.itemType,
                            quantity = cartItem.quantity,
                            serviceType = cartItem.serviceType,
                            pricePerItem = cartItem.pricePerItem
                        )
                    },
                    totalPrice = _state.value.totalPrice,
                    status = OrderStatus.PENDING,
                    scheduledDate = _state.value.selectedDate!!,
                    scheduledTime = _state.value.selectedTime!!,
                    createdAt = System.currentTimeMillis()
                )
                
                orderRepository.createOrder(order)
                    .onSuccess { orderId ->
                        // Clear cart after successful order
                        cartRepository.clearCart()
                        _state.update { 
                            it.copy(
                                isProcessing = false,
                                orderPlacedSuccessfully = true,
                                createdOrderId = orderId
                            ) 
                        }
                    }
                    .onFailure { exception ->
                        _state.update { 
                            it.copy(
                                isProcessing = false,
                                error = exception.message ?: "Failed to place order"
                            ) 
                        }
                    }
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        isProcessing = false,
                        error = e.message ?: "Failed to place order"
                    ) 
                }
            }
        }
    }
}
