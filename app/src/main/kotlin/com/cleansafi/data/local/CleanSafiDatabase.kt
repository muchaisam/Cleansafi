package com.cleansafi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cleansafi.data.local.dao.*
import com.cleansafi.data.local.entity.*

@Database(
    entities = [
        UserEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        CartItemEntity::class,
        PaymentEntity::class,
        OrderStatusHistoryEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class CleanSafiDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun orderDao(): OrderDao
    abstract fun cartDao(): CartDao
    abstract fun paymentDao(): PaymentDao
    abstract fun orderStatusHistoryDao(): OrderStatusHistoryDao
    
    companion object {
        const val DATABASE_NAME = "cleansafi_db"
    }
}
