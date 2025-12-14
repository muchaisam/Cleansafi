package com.cleansafi.presentation.payment

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cleansafi.domain.model.PaymentStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
        onNavigateBack: () -> Unit,
        onPaymentSuccess: () -> Unit,
        networkMonitor: com.cleansafi.core.network.NetworkMonitor,
        viewModel: PaymentViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isOnline by networkMonitor.isOnline.collectAsStateWithLifecycle(true)
    val haptic = com.cleansafi.core.ui.rememberHapticFeedback()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            kotlinx.coroutines.delay(2000)
            onPaymentSuccess()
        }
    }

    Scaffold(
            topBar = {
                TopAppBar(
                        title = { Text("Payment") },
                        navigationIcon = {
                            if (!state.isProcessing) {
                                IconButton(onClick = onNavigateBack) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                                }
                            }
                        },
                        colors =
                                TopAppBarDefaults.topAppBarColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                )
            }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (state.paymentStatus) {
                PaymentStatus.PENDING ->
                        PaymentInputView(
                                state = state,
                                onPhoneNumberChange = viewModel::onPhoneNumberChange,
                                onInitiatePayment = {
                                    haptic.click()
                                    viewModel.initiatePayment()
                                }
                        )
                PaymentStatus.PROCESSING ->
                        PaymentProcessingView(
                                state = state,
                                onSimulateSuccess = {
                                    haptic.success()
                                    viewModel.simulateSuccess()
                                },
                                onSimulateFailure = {
                                    haptic.error()
                                    viewModel.simulateFailure()
                                }
                        )
                PaymentStatus.SUCCESS -> PaymentSuccessView(state = state)
                PaymentStatus.FAILED ->
                        PaymentFailedView(
                                state = state,
                                onRetry = viewModel::initiatePayment,
                                onCancel = onNavigateBack
                        )
                else -> {}
            }
        }
    }
}

@Composable
fun PaymentInputView(
        state: PaymentState,
        onPhoneNumberChange: (String) -> Unit,
        onInitiatePayment: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        // M-PESA Branding
        Card(
                modifier = Modifier.fillMaxWidth(),
                colors =
                        CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
        ) {
            Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                        text = "M-PESA",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                )
                Text(text = "Lipa na M-PESA", style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Amount Card
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                        text = "Amount to Pay",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                        text = "KSh ${state.amount}",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Phone Number Input
        OutlinedTextField(
                value = state.phoneNumber,
                onValueChange = onPhoneNumberChange,
                label = { Text("M-PESA Phone Number") },
                placeholder = { Text("0712345678 or 254712345678") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = state.error != null
        )

        AnimatedVisibility(visible = state.error != null) {
            Text(
                    text = state.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Info Card
        Card(
                modifier = Modifier.fillMaxWidth(),
                colors =
                        CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
        ) {
            Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                )
                Column {
                    Text(
                            text = "How it works",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                            text =
                                    "1. Enter your M-PESA number\n" +
                                            "2. Click 'Pay Now'\n" +
                                            "3. Enter your M-PESA PIN on your phone\n" +
                                            "4. Confirm the payment",
                            style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Pay Button
        Button(
                onClick = onInitiatePayment,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = !state.isProcessing
        ) {
            if (state.isProcessing) {
                CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Icon(Icons.Default.Payment, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Pay KSh ${state.amount}", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun PaymentProcessingView(
        state: PaymentState,
        onSimulateSuccess: () -> Unit,
        onSimulateFailure: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by
            infiniteTransition.animateFloat(
                    initialValue = 0.9f,
                    targetValue = 1.1f,
                    animationSpec =
                            infiniteRepeatable(
                                    animation = tween(1000, easing = FastOutSlowInEasing),
                                    repeatMode = RepeatMode.Reverse
                            ),
                    label = "scale"
            )

    Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        // Phone Icon Animated
        Box(
                modifier =
                        Modifier.size(120.dp)
                                .scale(scale)
                                .background(
                                        MaterialTheme.colorScheme.primaryContainer,
                                        CircleShape
                                ),
                contentAlignment = Alignment.Center
        ) {
            Icon(
                    Icons.Default.PhoneAndroid,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
                text = "Check your phone",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
                text = "Enter your M-PESA PIN to complete payment",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
                text = state.formattedPhoneNumber,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Countdown
        CircularProgressIndicator(
                progress = state.countdown / 60f,
                modifier = Modifier.size(80.dp),
                strokeWidth = 6.dp,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
                text = "${state.countdown}s remaining",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Demo Controls
        if (state.showDemoControls) {
            Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                            CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                            )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                                Icons.Default.DeveloperMode,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                                text = "Demo Controls",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                                onClick = onSimulateSuccess,
                                modifier = Modifier.weight(1f),
                                colors =
                                        ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.primary
                                        )
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Success")
                        }

                        OutlinedButton(
                                onClick = onSimulateFailure,
                                modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Cancel, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Fail")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentSuccessView(state: PaymentState) {
    val scale = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
                targetValue = 1f,
                animationSpec =
                        spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                        )
        )
    }

    Column(
            modifier =
                    Modifier.fillMaxSize()
                            .background(
                                    Brush.verticalGradient(
                                            colors =
                                                    listOf(
                                                            MaterialTheme.colorScheme
                                                                    .primaryContainer,
                                                            MaterialTheme.colorScheme.surface
                                                    )
                                    )
                            )
                            .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        com.cleansafi.core.ui.SuccessAnimation(modifier = Modifier.size(120.dp))

        Spacer(modifier = Modifier.height(32.dp))

        Text(
                text = "Payment Successful!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                PaymentDetailRow("Amount Paid", "KSh ${state.amount}")
                Divider(modifier = Modifier.padding(vertical = 12.dp))
                PaymentDetailRow("Transaction ID", state.payment?.transactionId ?: "N/A")
                Divider(modifier = Modifier.padding(vertical = 12.dp))
                PaymentDetailRow("Phone Number", state.formattedPhoneNumber)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
                text = "Your order has been confirmed",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun PaymentFailedView(state: PaymentState, onRetry: () -> Unit, onCancel: () -> Unit) {
    Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        Icon(
                Icons.Default.Error,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
                text = "Payment Failed",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
                modifier = Modifier.fillMaxWidth(),
                colors =
                        CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                        )
        ) {
            Text(
                    text = state.payment?.failureReason ?: "Unknown error occurred",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onRetry, modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Try Again")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(onClick = onCancel, modifier = Modifier.fillMaxWidth()) {
            Text("Cancel Order")
        }
    }
}

@Composable
fun PaymentDetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
        )
    }
}
