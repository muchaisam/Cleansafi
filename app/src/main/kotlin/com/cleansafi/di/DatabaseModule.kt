package com.cleansafi.di

import android.content.Context
import androidx.room.Room
import com.cleansafi.data.local.CleanSafiDatabase
import com.cleansafi.data.local.dao.CartDao
import com.cleansafi.data.local.dao.OrderDao
import com.cleansafi.data.local.dao.OrderStatusHistoryDao
import com.cleansafi.data.local.dao.PaymentDao
import com.cleansafi.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideCleanSafiDatabase(
        @ApplicationContext context: Context
    ): CleanSafiDatabase {
        return Room.databaseBuilder(
            context,
            CleanSafiDatabase::class.java,
            CleanSafiDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    
    @Provides
    fun provideUserDao(database: CleanSafiDatabase): UserDao {
        return database.userDao()
    }
    
    @Provides
    fun provideOrderDao(database: CleanSafiDatabase): OrderDao {
        return database.orderDao()
    }
    
    @Provides
    fun provideCartDao(database: CleanSafiDatabase): CartDao {
        return database.cartDao()
    }
    
    @Provides
    fun providePaymentDao(database: CleanSafiDatabase): PaymentDao {
        return database.paymentDao()
    }
    
    @Provides
    fun provideOrderStatusHistoryDao(database: CleanSafiDatabase): OrderStatusHistoryDao {
        return database.orderStatusHistoryDao()
    }
}
