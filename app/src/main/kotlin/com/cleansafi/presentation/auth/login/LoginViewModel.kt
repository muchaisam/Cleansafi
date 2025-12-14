package com.cleansafi.presentation.auth.login

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
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email, emailError = null, error = null) }
    }

    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password, passwordError = null, error = null) }
    }

    fun onLoginClick() {
        if (!validateInputs()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            userRepository.login(
                email = _state.value.email.trim(),
                password = _state.value.password
            ).onSuccess { user ->
                preferencesManager.currentUserId = user.userId
                _state.update { it.copy(isLoading = false, isLoginSuccess = true) }
            }.onFailure { exception ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = exception.message ?: "Login failed"
                    )
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        val email = _state.value.email.trim()
        val password = _state.value.password

        var isValid = true

        // Validate email
        val emailValidation = Validators.validateEmail(email)
        if (!emailValidation.isValid) {
            _state.update { it.copy(emailError = emailValidation.errorMessage) }
            isValid = false
        }

        // Validate password
        val passwordValidation = Validators.validateRequired(password, "Password")
        if (!passwordValidation.isValid) {
            _state.update { it.copy(passwordError = passwordValidation.errorMessage) }
            isValid = false
        } else if (password.length < 6) {
            _state.update { it.copy(passwordError = "Password must be at least 6 characters") }
            isValid = false
        }

        return isValid
    }

    fun retry() {
        onLoginClick()
    }
}
