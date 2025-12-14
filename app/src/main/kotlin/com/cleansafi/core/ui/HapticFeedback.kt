package com.cleansafi.core.ui

import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView

class HapticFeedback(private val view: View) {
    fun click() {
        view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
    }

    fun longPress() {
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }

    fun success() {
        view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
    }

    fun error() {
        view.performHapticFeedback(HapticFeedbackConstants.REJECT)
    }
}

@Composable
fun rememberHapticFeedback(): HapticFeedback {
    val view = LocalView.current
    return remember(view) { HapticFeedback(view) }
}
