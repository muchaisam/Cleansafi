package com.cleansafi.di

import com.cleansafi.data.repository.*
import com.cleansafi.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
    
    @Binds
    @Singleton
    abstract fun bindCartRepository(
        cartRepositoryImpl: CartRepositoryImpl
    ): CartRepository
    
    @Binds
    @Singleton
    abstract fun bindOrderRepository(
        orderRepositoryImpl: OrderRepositoryImpl
    ): OrderRepository
    
    @Binds
    @Singleton
    abstract fun bindPaymentRepository(
        paymentRepositoryImpl: PaymentRepositoryImpl
    ): PaymentRepository
    
    @Binds
    @Singleton
    abstract fun bindOrderStatusRepository(
        orderStatusRepositoryImpl: OrderStatusRepositoryImpl
    ): OrderStatusRepository
}
