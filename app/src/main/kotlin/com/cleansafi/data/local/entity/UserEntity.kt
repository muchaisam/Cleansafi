package com.cleansafi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val userId: String,
    val email: String,
    val name: String,
    val phoneNumber: String,
    val passwordHash: String,
    val createdAt: Long
)
