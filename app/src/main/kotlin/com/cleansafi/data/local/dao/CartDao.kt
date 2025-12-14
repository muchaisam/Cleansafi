package com.cleansafi.data.local.dao

import androidx.room.*
import com.cleansafi.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun observeCartItems(): Flow<List<CartItemEntity>>
    
    @Query("SELECT * FROM cart_items")
    suspend fun getCartItems(): List<CartItemEntity>
    
    @Upsert
    suspend fun upsertCartItem(item: CartItemEntity)
    
    @Delete
    suspend fun deleteCartItem(item: CartItemEntity)
    
    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
    
    @Query("SELECT SUM(quantity * pricePerItem) FROM cart_items")
    fun observeCartTotal(): Flow<Int?>
    
    @Query("SELECT SUM(quantity * pricePerItem) FROM cart_items")
    suspend fun getCartTotal(): Int?
}
