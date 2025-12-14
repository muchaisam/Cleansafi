package com.cleansafi.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleansafi.core.error.toAppError
import com.cleansafi.core.util.PreferencesManager
import com.cleansafi.domain.model.User
import com.cleansafi.domain.repository.OrderRepository
import com.cleansafi.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository,
    private val preferencesManager: PreferencesManager,
    private val themeManager: com.cleansafi.core.theme.ThemeManager
) : ViewModel() {
    
    fun getThemeManager() = themeManager
    
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()
    
    init {
        loadProfile()
    }
    
    private fun loadProfile() {
        val userId = preferencesManager.currentUserId ?: return
        
        viewModelScope.launch {
            combine(
                userRepository.observeCurrentUser(userId),
                orderRepository.observeUserOrders(userId),
                orderRepository.observeTotalSpent(userId)
            ) { user, orders, totalSpent ->
                ProfileState(
                    user = user,
                    isLoading = false,
                    totalOrders = orders.size,
                    totalSpent = totalSpent,
                    error = null
                )
            }
            .catch { e -> _state.update { it.copy(error = e.message ?: "Error loading profile", isLoading = false) } }
            .collect { newState ->
                _state.value = newState
            }
        }
    }
    
    fun enterEditMode() {
        val user = _state.value.user ?: return
        _state.update { 
            it.copy(
                isEditMode = true,
                editName = user.name,
                editPhoneNumber = user.phoneNumber
            ) 
        }
    }
    
    fun cancelEdit() {
        _state.update { 
            it.copy(
                isEditMode = false,
                editName = "",
                editPhoneNumber = "",
                nameError = null,
                phoneError = null
            ) 
        }
    }
    
    fun onNameChange(name: String) {
        _state.update { it.copy(editName = name, nameError = null) }
    }
    
    fun onPhoneNumberChange(phoneNumber: String) {
        _state.update { it.copy(editPhoneNumber = phoneNumber, phoneError = null) }
    }
    
    fun saveProfile() {
        if (!validateInputs()) return
        
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null) }
            
            try {
                val user = _state.value.user ?: throw Exception("User not found")
                
                val updatedUser = user.copy(
                    name = _state.value.editName.trim(),
                    phoneNumber = _state.value.editPhoneNumber.trim()
                )
                
                userRepository.updateUser(updatedUser)
                
                _state.update { 
                    it.copy(
                        isSaving = false,
                        isEditMode = false,
                        editName = "",
                        editPhoneNumber = ""
                    ) 
                }
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        isSaving = false,
                        error = e.message ?: "Failed to update profile"
                    ) 
                }
            }
        }
    }
    
    private fun validateInputs(): Boolean {
        val name = _state.value.editName.trim()
        val phoneNumber = _state.value.editPhoneNumber.trim()
        
        var isValid = true
        
        if (name.isEmpty()) {
            _state.update { it.copy(nameError = "Name is required") }
            isValid = false
        } else if (name.length < 2) {
            _state.update { it.copy(nameError = "Name must be at least 2 characters") }
            isValid = false
        }
        
        if (phoneNumber.isEmpty()) {
            _state.update { it.copy(phoneError = "Phone number is required") }
            isValid = false
        } else if (phoneNumber.length < 10) {
            _state.update { it.copy(phoneError = "Invalid phone number") }
            isValid = false
        }
        
        return isValid
    }
    
    fun showLogoutDialog() {
        _state.update { it.copy(showLogoutDialog = true) }
    }
    
    fun hideLogoutDialog() {
        _state.update { it.copy(showLogoutDialog = false) }
    }
    
    fun logout() {
        preferencesManager.clear()
    }
}
