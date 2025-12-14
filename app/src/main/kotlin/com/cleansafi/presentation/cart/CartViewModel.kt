package com.cleansafi.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleansafi.domain.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CartViewModel @Inject constructor(private val cartRepository: CartRepository) : ViewModel() {

    private val _state = MutableStateFlow(CartState())
    val state = _state.asStateFlow()

    init {
        observeCart()
    }

    private fun observeCart() {
        viewModelScope.launch {
            combine(cartRepository.observeCartItems(), cartRepository.observeCartTotal()) {
                    items,
                    total ->
                CartState(
                        items = items,
                        totalPrice = total,
                        isLoading = false,
                        suggestedItems = generateSuggestions(items),
                        error = null
                )
            }
                    .catch { e ->
                        _state.update {
                            it.copy(error = e.message ?: "Error loading cart", isLoading = false)
                        }
                    }
                    .collect { newState -> _state.value = newState }
        }
    }

    fun retry() {
        _state.update { it.copy(error = null) }
        observeCart()
    }

    private fun generateSuggestions(
            cartItems: List<com.cleansafi.domain.model.CartItem>
    ): List<com.cleansafi.presentation.cart.SuggestedItem> {
        if (cartItems.isEmpty()) return emptyList()

        val currentTypes = cartItems.map { it.itemType }.toSet()
        val suggestions = mutableListOf<com.cleansafi.presentation.cart.SuggestedItem>()

        // Cross-sell: If they have shirts, suggest pants
        if (currentTypes.contains(com.cleansafi.domain.model.LaundryItemType.OTHERS) &&
                        !currentTypes.contains(com.cleansafi.domain.model.LaundryItemType.TROUSERS)
        ) {
            suggestions.add(
                    com.cleansafi.presentation.cart.SuggestedItem(
                            itemType = com.cleansafi.domain.model.LaundryItemType.TROUSERS,
                            reason = "Complete your outfit",
                            discountPercent = 10
                    )
            )
        }

        // Cross-sell: If they have clothes, suggest bedsheets
        if ((currentTypes.contains(com.cleansafi.domain.model.LaundryItemType.TOPS) ||
                        currentTypes.contains(
                                com.cleansafi.domain.model.LaundryItemType.TROUSERS
                        )) &&
                        !currentTypes.contains(com.cleansafi.domain.model.LaundryItemType.BEDSHEETS)
        ) {
            suggestions.add(
                    com.cleansafi.presentation.cart.SuggestedItem(
                            itemType = com.cleansafi.domain.model.LaundryItemType.BEDSHEETS,
                            reason = "Bundle discount available",
                            discountPercent = 15
                    )
            )
        }

        // Upsell: Suggest premium items if only basic
        if (currentTypes.size < 3) {
            val suggestedTypes = suggestions.map { it.itemType }.toSet()
            val notInCart =
                    com.cleansafi.domain.model.LaundryItemType.entries.filter {
                        it !in currentTypes && it !in suggestedTypes
                    }
            notInCart.take(2).forEach { itemType ->
                suggestions.add(
                        com.cleansafi.presentation.cart.SuggestedItem(
                                itemType = itemType,
                                reason = "Customers also cleaned",
                                discountPercent = 0
                        )
                )
            }
        }

        return suggestions.distinctBy { it.itemType }.take(3) // Show max 3 unique suggestions
    }

    fun addSuggestedItem(itemType: com.cleansafi.domain.model.LaundryItemType) {
        val cartItems = _state.value.items
        if (cartItems.isEmpty()) return

        val serviceType = cartItems.first().serviceType
        val quantity = 1
        val pricePerItem =
                com.cleansafi.presentation.order.placeorder.PricingCalculator.calculateItemPrice(
                        itemType,
                        serviceType
                )

        viewModelScope.launch {
            cartRepository.addToCart(
                    com.cleansafi.domain.model.CartItem(
                            itemType = itemType,
                            quantity = quantity,
                            serviceType = serviceType,
                            pricePerItem = pricePerItem
                    )
            )
        }
    }

    fun removeFromCart(itemType: String) {
        viewModelScope.launch { cartRepository.removeFromCart(itemType) }
    }

    fun clearCart() {
        viewModelScope.launch { cartRepository.clearCart() }
    }
}
