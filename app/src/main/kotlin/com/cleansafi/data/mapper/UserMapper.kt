package com.cleansafi.data.mapper

import com.cleansafi.data.local.entity.UserEntity
import com.cleansafi.domain.model.User

fun UserEntity.toDomain(): User {
    return User(
        userId = userId,
        email = email,
        name = name,
        phoneNumber = phoneNumber
    )
}

fun User.toEntity(passwordHash: String, createdAt: Long): UserEntity {
    return UserEntity(
        userId = userId,
        email = email,
        name = name,
        phoneNumber = phoneNumber,
        passwordHash = passwordHash,
        createdAt = createdAt
    )
}
