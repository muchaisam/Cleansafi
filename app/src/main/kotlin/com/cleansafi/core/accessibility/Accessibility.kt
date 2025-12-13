package com.cleansafi.core.accessibility

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

/**
 * Accessibility utilities for screen readers and assistive technologies
 */

// Minimum touch target size (Material Design guidelines)
const val MIN_TOUCH_TARGET_SIZE = 48 // dp

// Content descriptions for common UI elements
object ContentDescriptions {
    // Navigation
    const val NAVIGATE_BACK = "Navigate back"
    const val NAVIGATE_TO_CART = "Go to cart"
    const val NAVIGATE_TO_PROFILE = "Go to profile"
    const val NAVIGATE_TO_ORDERS = "Go to orders"
    
    // Actions
    const val ADD_TO_CART = "Add item to cart"
    const val REMOVE_FROM_CART = "Remove item from cart"
    const val INCREASE_QUANTITY = "Increase quantity"
    const val DECREASE_QUANTITY = "Decrease quantity"
    const val PLACE_ORDER = "Place order"
    const val PROCEED_TO_CHECKOUT = "Proceed to checkout"
    const val PAY_NOW = "Pay now"
    const val RETRY = "Retry action"
    
    // Form fields
    const val EMAIL_INPUT = "Email address input"
    const val PASSWORD_INPUT = "Password input"
    const val PHONE_INPUT = "Phone number input"
    const val NAME_INPUT = "Name input"
    const val ADDRESS_INPUT = "Address input"
    
    // Status indicators
    const val LOADING = "Loading content"
    const val ERROR_OCCURRED = "Error occurred"
    const val SUCCESS = "Action completed successfully"
    const val OFFLINE = "You are currently offline"
    
    // Theme
    const val TOGGLE_DARK_MODE = "Toggle dark mode"
    const val TOGGLE_LIGHT_MODE = "Toggle light mode"
    
    // Notifications
    const val NOTIFICATION_PERMISSION_REQUIRED = "Notification permission required"
    const val ENABLE_NOTIFICATIONS = "Enable notifications"
}

// Helper function to create accessible content description
fun Modifier.accessibleContentDescription(description: String): Modifier {
    return this.semantics { contentDescription = description }
}
