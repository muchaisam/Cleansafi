package com.cleansafi.data.repository

import com.cleansafi.data.local.dao.UserDao
import com.cleansafi.data.mapper.toDomain
import com.cleansafi.data.mapper.toEntity
import com.cleansafi.domain.model.User
import com.cleansafi.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {
    
    override suspend fun getUserById(userId: String): User? {
        return userDao.getUserById(userId)?.toDomain()
    }
    
    override suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)?.toDomain()
    }
    
    override suspend fun createUser(
        email: String,
        password: String,
        name: String,
        phoneNumber: String
    ): Result<User> {
        return try {
            // Check if email already exists
            if (userDao.getUserByEmail(email) != null) {
                return Result.failure(Exception("Email already registered"))
            }
            
            val userId = UUID.randomUUID().toString()
            val passwordHash = hashPassword(password)
            val user = User(
                userId = userId,
                email = email,
                name = name,
                phoneNumber = phoneNumber
            )
            
            userDao.insertUser(user.toEntity(passwordHash, System.currentTimeMillis()))
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val userEntity = userDao.getUserByEmail(email)
                ?: return Result.failure(Exception("User not found"))
            
            val passwordHash = hashPassword(password)
            if (userEntity.passwordHash != passwordHash) {
                return Result.failure(Exception("Invalid password"))
            }
            
            Result.success(userEntity.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateUser(user: User) {
        val existingEntity = userDao.getUserById(user.userId)
            ?: throw Exception("User not found")
        
        userDao.updateUser(
            user.toEntity(
                passwordHash = existingEntity.passwordHash,
                createdAt = existingEntity.createdAt
            )
        )
    }
    
    override suspend fun deleteUser(userId: String) {
        userDao.getUserById(userId)?.let { userDao.deleteUser(it) }
    }
    
    override fun observeCurrentUser(userId: String): Flow<User?> {
        return userDao.observeUser(userId).map { it?.toDomain() }
    }
    
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray())
        return hash.fold("") { str, it -> str + "%02x".format(it) }
    }
}
