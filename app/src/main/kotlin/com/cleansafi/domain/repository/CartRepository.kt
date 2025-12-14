package com.cleansafi.domain.repository

import com.cleansafi.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun observeCartItems(): Flow<List<CartItem>>
    suspend fun getCartItems(): List<CartItem>
    suspend fun addToCart(item: CartItem)
    suspend fun removeFromCart(itemType: String)
    suspend fun clearCart()
    fun observeCartTotal(): Flow<Int>
    suspend fun getCartTotal(): Int
}
