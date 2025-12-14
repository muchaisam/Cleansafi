package com.cleansafi.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleansafi.core.util.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _state = MutableStateFlow(OnboardingState())
    val state = _state.asStateFlow()
    
    init {
        checkOnboardingStatus()
    }
    
    private fun checkOnboardingStatus() {
        val completed = preferencesManager.hasCompletedOnboarding
        _state.update { it.copy(hasCompletedOnboarding = completed) }
    }
    
    fun onPageChange(page: Int) {
        _state.update { it.copy(currentPage = page) }
    }
    
    fun completeOnboarding() {
        viewModelScope.launch {
            preferencesManager.hasCompletedOnboarding = true
            _state.update { it.copy(hasCompletedOnboarding = true) }
        }
    }
}
