package com.cleansafi.domain.repository

import com.cleansafi.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserById(userId: String): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun createUser(email: String, password: String, name: String, phoneNumber: String): Result<User>
    suspend fun login(email: String, password: String): Result<User>
    suspend fun updateUser(user: User)
    suspend fun deleteUser(userId: String)
    fun observeCurrentUser(userId: String): Flow<User?>
}
