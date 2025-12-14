package com.cleansafi.presentation.profile

import com.cleansafi.domain.model.User

data class ProfileState(
    val user: User? = null,
    val isLoading: Boolean = true,
    val isEditMode: Boolean = false,
    val editName: String = "",
    val editPhoneNumber: String = "",
    val nameError: String? = null,
    val phoneError: String? = null,
    val isSaving: Boolean = false,
    val error: String? = null,
    val showLogoutDialog: Boolean = false,
    val totalOrders: Int = 0,
    val totalSpent: Int = 0
)
