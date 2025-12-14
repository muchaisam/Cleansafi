package com.cleansafi.presentation.onboarding

data class OnboardingState(
    val hasCompletedOnboarding: Boolean = false,
    val currentPage: Int = 0
)

data class OnboardingPage(
    val title: String,
    val description: String,
    val emoji: String
)

val onboardingPages = listOf(
    OnboardingPage(
        title = "Welcome to CleanSafi",
        description = "Your laundry, simplified. Get professional cleaning and delivery right to your doorstep.",
        emoji = "🧺"
    ),
    OnboardingPage(
        title = "Easy Ordering",
        description = "Select your items, choose your service type, and we'll handle the rest. It's that simple!",
        emoji = "📱"
    ),
    OnboardingPage(
        title = "Track Your Order",
        description = "Get real-time updates from pickup to delivery. Stay informed every step of the way.",
        emoji = "📦"
    ),
    OnboardingPage(
        title = "Secure Payment",
        description = "Pay safely with M-PESA. Your transactions are secure and protected.",
        emoji = "💳"
    )
)
