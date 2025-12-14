package com.cleansafi.presentation.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
        onNavigateToOnboarding: () -> Unit,
        onNavigateToAuth: () -> Unit,
        onNavigateToHome: () -> Unit
) {
        val context = androidx.compose.ui.platform.LocalContext.current
        val preferencesManager = remember { com.cleansafi.core.util.PreferencesManager(context) }
        val scale = remember { Animatable(0f) }
        val alpha = remember { Animatable(0f) }

        LaunchedEffect(Unit) {
                // Animate logo scale
                scale.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
                )
                // Fade in text
                alpha.animateTo(targetValue = 1f, animationSpec = tween(durationMillis = 300))
                // Small delay then navigate
                delay(800)

                // Check navigation destination based on user state
                when {
                        // User is already logged in - go to home
                        preferencesManager.isLoggedIn -> onNavigateToHome()
                        // User completed onboarding but not logged in - go to login
                        preferencesManager.hasCompletedOnboarding -> onNavigateToAuth()
                        // First time user - show onboarding
                        else -> onNavigateToOnboarding()
                }
        }

        Box(
                modifier =
                        Modifier.fillMaxSize()
                                .background(
                                        brush =
                                                Brush.verticalGradient(
                                                        colors =
                                                                listOf(
                                                                        MaterialTheme.colorScheme
                                                                                .primary,
                                                                        MaterialTheme.colorScheme
                                                                                .primaryContainer
                                                                )
                                                )
                                ),
                contentAlignment = Alignment.Center
        ) {
                Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.scale(scale.value)
                ) {
                        // Animated logo
                        Box(
                                modifier =
                                        Modifier.size(120.dp)
                                                .scale(scale.value)
                                                .background(
                                                        color = MaterialTheme.colorScheme.onPrimary,
                                                        shape = MaterialTheme.shapes.extraLarge
                                                ),
                                contentAlignment = Alignment.Center
                        ) {
                                Text(
                                        text = "🧺",
                                        fontSize = 60.sp,
                                        modifier = Modifier.scale(alpha.value)
                                )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                                text = "Cleansafi",
                                style =
                                        MaterialTheme.typography.displayMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimary
                                        )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                                text = "Fresh & Clean, Anytime",
                                style =
                                        MaterialTheme.typography.titleMedium.copy(
                                                color =
                                                        MaterialTheme.colorScheme.onPrimary.copy(
                                                                alpha = 0.8f
                                                        )
                                        )
                        )
                }
        }
}
