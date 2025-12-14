package com.cleansafi.presentation.checkout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
        onNavigateBack: () -> Unit,
        onNavigateToPayment: (orderId: Long, amount: Int) -> Unit,
        networkMonitor: com.cleansafi.core.network.NetworkMonitor,
        viewModel: CheckoutViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isOnline by networkMonitor.isOnline.collectAsStateWithLifecycle(true)
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    LaunchedEffect(state.orderPlacedSuccessfully) {
        if (state.orderPlacedSuccessfully && state.createdOrderId != null) {
            onNavigateToPayment(state.createdOrderId!!, state.totalPrice)
        }
    }

    Scaffold(
            topBar = {
                TopAppBar(
                        title = { Text("Checkout") },
                        navigationIcon = {
                            IconButton(onClick = onNavigateBack) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                        },
                        colors =
                                TopAppBarDefaults.topAppBarColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        titleContentColor =
                                                MaterialTheme.colorScheme.onPrimaryContainer
                                )
                )
            },
            bottomBar = {
                BottomAppBar(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                    Column(
                            modifier =
                                    Modifier.fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Button(
                                onClick = viewModel::placeOrder,
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                enabled = state.canProceed && !state.isProcessing
                        ) {
                            if (state.isProcessing) {
                                CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                Icon(
                                        Icons.Default.Payment,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                        text = "Proceed to Payment - KSh ${state.totalPrice}",
                                        style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
            }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                    modifier =
                            Modifier.fillMaxSize()
                                    .padding(paddingValues)
                                    .verticalScroll(rememberScrollState())
                                    .padding(16.dp)
            ) {
                // Order Summary Card
                Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors =
                                CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                                text = "Order Summary",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                    text = "Service Type:",
                                    style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                    text =
                                            state.serviceType
                                                    .replace("_", " ")
                                                    .lowercase()
                                                    .replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Total Items:", style = MaterialTheme.typography.bodyMedium)
                            Text(
                                    text = state.items.sumOf { it.quantity }.toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                            )
                        }

                        Divider(modifier = Modifier.padding(vertical = 12.dp))

                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                    text = "Total Amount:",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                            )
                            Text(
                                    text = "KSh ${state.totalPrice}",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Schedule Pickup Section
                Text(
                        text = "Schedule Pickup",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Date Selection
                OutlinedCard(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = viewModel::showDatePicker
                ) {
                    Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                    text = "Pickup Date",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                    text =
                                            state.selectedDate?.let {
                                                SimpleDateFormat(
                                                                "MMM dd, yyyy",
                                                                Locale.getDefault()
                                                        )
                                                        .format(Date(it))
                                            }
                                                    ?: "Select date",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium
                            )
                        }

                        Icon(
                                Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Time Selection
                OutlinedCard(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = viewModel::showTimePicker
                ) {
                    Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                    text = "Pickup Time",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                    text = state.selectedTime ?: "Select time",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium
                            )
                        }

                        Icon(
                                Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Error Message
                AnimatedVisibility(visible = state.error != null) {
                    Card(
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                            colors =
                                    CardDefaults.cardColors(
                                            containerColor =
                                                    MaterialTheme.colorScheme.errorContainer
                                    )
                    ) {
                        Text(
                                text = state.error ?: "",
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Items List
                Text(
                        text = "Items",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                state.items.forEach { item ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                        text = item.itemType.displayName,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                )
                                Text(
                                        text = "${item.quantity} × KSh ${item.pricePerItem}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Text(
                                    text = "KSh ${item.quantity * item.pricePerItem}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // Date Picker Dialog
        if (state.showDatePicker) {
            DatePickerDialog(
                    onDismissRequest = viewModel::hideDatePicker,
                    confirmButton = {
                        TextButton(
                                onClick = {
                                    datePickerState.selectedDateMillis?.let {
                                        viewModel.onDateSelected(it)
                                    }
                                }
                        ) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = viewModel::hideDatePicker) { Text("Cancel") }
                    }
            ) {
                DatePicker(
                        state = datePickerState,
                        dateValidator = { timestamp ->
                            timestamp >= System.currentTimeMillis() - 86400000 // Allow from today
                        }
                )
            }
        }

        // Time Picker Dialog
        if (state.showTimePicker) {
            AlertDialog(
                    onDismissRequest = viewModel::hideTimePicker,
                    confirmButton = {
                        TextButton(
                                onClick = {
                                    viewModel.onTimeSelected(
                                            timePickerState.hour,
                                            timePickerState.minute
                                    )
                                }
                        ) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = viewModel::hideTimePicker) { Text("Cancel") }
                    },
                    text = { TimePicker(state = timePickerState) }
            )
        }

        com.cleansafi.core.ui.OfflineIndicator(
                networkMonitor = networkMonitor,
                modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
