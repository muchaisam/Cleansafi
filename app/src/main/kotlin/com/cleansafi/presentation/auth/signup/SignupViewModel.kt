package com.cleansafi.presentation.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleansafi.core.util.PreferencesManager
import com.cleansafi.core.validation.Validators
import com.cleansafi.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _state = MutableStateFlow(SignupState())
    val state = _state.asStateFlow()
    
    fun onNameChange(name: String) {
        _state.update { it.copy(name = name, nameError = null, error = null) }
    }
    
    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email, emailError = null, error = null) }
    }
    
    fun onPhoneNumberChange(phoneNumber: String) {
        _state.update { it.copy(phoneNumber = phoneNumber, phoneNumberError = null, error = null) }
    }
    
    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password, passwordError = null, error = null) }
    }
    
    fun onConfirmPasswordChange(confirmPassword: String) {
        _state.update { it.copy(confirmPassword = confirmPassword, confirmPasswordError = null, error = null) }
    }
    
    fun onSignupClick() {
        if (!validateInputs()) return
        
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            userRepository.createUser(
                email = _state.value.email.trim(),
                password = _state.value.password,
                name = _state.value.name.trim(),
                phoneNumber = _state.value.phoneNumber.trim()
            ).onSuccess { user ->
                preferencesManager.currentUserId = user.userId
                _state.update { it.copy(isLoading = false, isSignupSuccess = true) }
            }.onFailure { exception ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = exception.message ?: "Signup failed"
                    )
                }
            }
        }
    }
    
    private fun validateInputs(): Boolean {
        val name = _state.value.name.trim()
        val email = _state.value.email.trim()
        val phoneNumber = _state.value.phoneNumber.trim()
        val password = _state.value.password
        val confirmPassword = _state.value.confirmPassword
        
        var isValid = true
        
        // Validate name
        val nameValidation = Validators.validateName(name, "Full name")
        if (!nameValidation.isValid) {
            _state.update { it.copy(nameError = nameValidation.errorMessage) }
            isValid = false
        }
        
        // Validate email
        val emailValidation = Validators.validateEmail(email)
        if (!emailValidation.isValid) {
            _state.update { it.copy(emailError = emailValidation.errorMessage) }
            isValid = false
        }
        
        // Validate phone number
        val phoneValidation = Validators.validatePhoneNumber(phoneNumber)
        if (!phoneValidation.isValid) {
            _state.update { it.copy(phoneNumberError = phoneValidation.errorMessage) }
            isValid = false
        }
        
        // Validate password
        val passwordValidation = Validators.validatePassword(password)
        if (!passwordValidation.isValid) {
            _state.update { it.copy(passwordError = passwordValidation.errorMessage) }
            isValid = false
        }
        
        // Validate password confirmation
        val confirmPasswordValidation = Validators.validatePasswordConfirmation(password, confirmPassword)
        if (!confirmPasswordValidation.isValid) {
            _state.update { it.copy(confirmPasswordError = confirmPasswordValidation.errorMessage) }
            isValid = false
        }
        
        return isValid
    }
    
    fun retry() {
        onSignupClick()
    }
}
