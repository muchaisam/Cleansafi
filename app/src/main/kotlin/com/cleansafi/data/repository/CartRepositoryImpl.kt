package com.cleansafi.data.repository

import com.cleansafi.data.local.dao.CartDao
import com.cleansafi.data.mapper.toDomain
import com.cleansafi.data.mapper.toEntity
import com.cleansafi.domain.model.CartItem
import com.cleansafi.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
) : CartRepository {

    override fun observeCartItems(): Flow<List<CartItem>> {
        return cartDao.observeCartItems().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getCartItems(): List<CartItem> {
        return cartDao.getCartItems().map { it.toDomain() }
    }

    override suspend fun addToCart(item: CartItem) {
        cartDao.upsertCartItem(item.toEntity())
    }

    override suspend fun removeFromCart(itemType: String) {
        val entities = cartDao.getCartItems()
        entities.find { it.itemType == itemType }?.let {
            cartDao.deleteCartItem(it)
        }
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }

    override fun observeCartTotal(): Flow<Int> {
        return cartDao.observeCartTotal().map { it ?: 0 }
    }

    override suspend fun getCartTotal(): Int {
        return cartDao.getCartTotal() ?: 0
    }
}
