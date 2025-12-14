package com.cleansafi.presentation.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleansafi.core.error.toAppError
import com.cleansafi.core.util.PreferencesManager
import com.cleansafi.domain.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel
@Inject
constructor(
    private val orderRepository: OrderRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _state = MutableStateFlow(OrdersState())
    val state = _state.asStateFlow()

    init {
        loadOrders()
    }

    private fun loadOrders() {
        val userId = preferencesManager.currentUserId ?: return

        viewModelScope.launch {
            try {
                combine(
                    orderRepository.observeUserOrders(userId),
                    orderRepository.observeTotalSpent(userId)
                ) { orders, totalSpent ->
                    OrdersState(
                        orders = orders,
                        totalSpent = totalSpent,
                        isLoading = false,
                        error = null
                    )
                }
                    .collect { newState -> _state.value = newState }
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.toAppError())
            }
        }
    }

    fun retry() {
        _state.value = OrdersState(isLoading = true)
        loadOrders()
    }
}
