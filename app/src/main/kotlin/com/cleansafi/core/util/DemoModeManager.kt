package com.cleansafi.core.util

import android.content.Context

/**
 * Manages demo mode for faster order progression during testing. In demo mode, order status updates
 * happen much faster for demonstration purposes.
 */
class DemoModeManager(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var isDemoMode: Boolean
        get() = prefs.getBoolean(KEY_DEMO_MODE, true) // Default to demo mode for portfolio app
        set(value) = prefs.edit().putBoolean(KEY_DEMO_MODE, value).apply()

    /**
     * Returns the delay in seconds for order status progression. In demo mode, delays are
     * significantly reduced.
     *
     * @param normalDelayMinutes The normal delay in minutes
     * @return The actual delay in seconds to use
     */
    fun getProgressionDelaySeconds(normalDelayMinutes: Long): Long {
        return if (isDemoMode) {
            // In demo mode, use seconds instead of minutes (much faster)
            normalDelayMinutes * 5 // 5 seconds per "minute" in demo mode
        } else {
            // In production mode, use actual minutes converted to seconds
            normalDelayMinutes * 60
        }
    }

    companion object {
        private const val PREFS_NAME = "cleansafi_demo_prefs"
        private const val KEY_DEMO_MODE = "demo_mode_enabled"
    }
}
