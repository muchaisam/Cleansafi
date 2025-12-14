package com.cleansafi.presentation.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cleansafi.core.ui.rememberHapticFeedback
import com.cleansafi.domain.model.Order
import com.cleansafi.domain.model.OrderStatus
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(onNavigateBack: () -> Unit, viewModel: OrdersViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
            topBar = {
                TopAppBar(
                        title = { Text("Order History") },
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
            }
    ) { paddingValues ->
        val haptic = rememberHapticFeedback()

        if (state.isLoading) {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                items(5) { com.cleansafi.core.ui.ShimmerOrderCard() }
            }
        } else if (state.error != null) {
            val currentError = state.error
            com.cleansafi.core.error.ErrorView(
                    error = currentError,
                    onRetry = viewModel::retry,
                    modifier = Modifier.fillMaxSize().padding(paddingValues)
            )
        } else if (state.orders.isEmpty()) {
            com.cleansafi.core.ui.EmptyOrdersState(
                    onPlaceOrder = onNavigateBack,
                    modifier = Modifier.fillMaxSize().padding(paddingValues)
            )
        } else {
            LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { StatsCard(totalOrders = state.orders.size, totalSpent = state.totalSpent) }

                item {
                    Text(
                            text = "Your Orders",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(items = state.orders, key = { it.orderId }) { order ->
                    OrderCard(order = order)
                }
            }
        }
    }
}

@Composable
fun EmptyOrdersView(modifier: Modifier = Modifier) {
    Column(
            modifier = modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        Text(text = "📦", style = MaterialTheme.typography.displayLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
                text = "No orders yet",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
                text = "Your order history will appear here",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
        )
    }
}

@Composable
fun StatsCard(totalOrders: Int, totalSpent: Int) {
    Card(
            modifier = Modifier.fillMaxWidth(),
            colors =
                    CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
    ) {
        Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
        ) {
            StatItem(
                    icon = Icons.Default.ShoppingBag,
                    label = "Total Orders",
                    value = totalOrders.toString()
            )

            HorizontalDivider(
                    modifier = Modifier.height(60.dp).width(1.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
            )

            StatItem(
                    icon = Icons.Default.Payments,
                    label = "Total Spent",
                    value = "KSh $totalSpent"
            )
        }
    }
}

@Composable
fun StatItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
        )
        Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun OrderCard(order: Order) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                            text = "Order #${order.orderId}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                    )
                    Text(
                            text =
                                    SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                                            .format(Date(order.createdAt)),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                OrderStatusChip(status = order.status)
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                            text = "Items: ${order.items.sumOf { it.quantity }}",
                            style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                            text =
                                    "Scheduled: ${order.scheduledTime} on ${
                            SimpleDateFormat("MMM dd", Locale.getDefault())
                                .format(Date(order.scheduledDate))
                        }",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                        text = "KSh ${order.totalPrice}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun OrderStatusChip(status: OrderStatus) {
    val (color, text) =
            when (status) {
                OrderStatus.PENDING -> MaterialTheme.colorScheme.tertiary to status.displayName
                OrderStatus.PAYMENT_FAILED -> MaterialTheme.colorScheme.error to status.displayName
                OrderStatus.CONFIRMED -> MaterialTheme.colorScheme.primary to status.displayName
                OrderStatus.PICKED_UP -> MaterialTheme.colorScheme.secondary to status.displayName
                OrderStatus.PROCESSING -> MaterialTheme.colorScheme.secondary to status.displayName
                OrderStatus.READY ->
                        MaterialTheme.colorScheme.primaryContainer to status.displayName
                OrderStatus.OUT_FOR_DELIVERY ->
                        MaterialTheme.colorScheme.tertiary to status.displayName
                OrderStatus.DELIVERED -> Color(0xFF00A651) to status.displayName
                OrderStatus.COMPLETED ->
                        MaterialTheme.colorScheme.primaryContainer to status.displayName
                OrderStatus.CANCELLED -> MaterialTheme.colorScheme.error to status.displayName
            }

    Surface(color = color, shape = MaterialTheme.shapes.small) {
        Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = status.emoji, style = MaterialTheme.typography.labelSmall)
            Text(
                    text = text,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
            )
        }
    }
}
