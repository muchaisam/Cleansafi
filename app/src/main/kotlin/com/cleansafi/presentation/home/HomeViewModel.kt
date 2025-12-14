package com.cleansafi.presentation.home

import androidx.lifecycle.ViewModel
import com.cleansafi.core.util.DemoModeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val demoModeManager: DemoModeManager
) : ViewModel() {
    
    private val _isDemoMode = MutableStateFlow(demoModeManager.isDemoModeEnabled)
    val isDemoMode = _isDemoMode.asStateFlow()
    
    fun toggleDemoMode() {
        demoModeManager.toggle()
        _isDemoMode.value = demoModeManager.isDemoModeEnabled
    }
}
