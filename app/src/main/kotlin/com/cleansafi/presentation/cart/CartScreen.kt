package com.cleansafi.presentation.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDismissState
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
import com.cleansafi.domain.model.CartItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCheckout: () -> Unit,
    networkMonitor: com.cleansafi.core.network.NetworkMonitor,
    viewModel: CartViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isOnline by networkMonitor.isOnline.collectAsStateWithLifecycle(true)
    val haptic = rememberHapticFeedback()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Cart") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (state.items.isNotEmpty()) {
                        IconButton(onClick = viewModel::clearCart) {
                            Icon(Icons.Default.Delete, contentDescription = "Clear cart")
                        }
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
            if (state.items.isNotEmpty()) {
                BottomAppBar(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                    Column(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Total Amount",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "KSh ${state.totalPrice}",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button(
                                onClick = onNavigateToCheckout,
                                modifier = Modifier.height(48.dp)
                            ) {
                                Text("Proceed to Checkout")
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (state.items.isEmpty()) {
                com.cleansafi.core.ui.EmptyCartState(
                    onAddItems = onNavigateBack,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item { ServiceTypeCard(serviceType = state.serviceType) }

                    item {
                        Text(
                            text = "Items (${state.items.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    items(items = state.items, key = { "cart_${it.itemType}" }) { item ->
                        val dismissState =
                            rememberDismissState(
                                confirmValueChange = {
                                    if (it == DismissValue.DismissedToStart) {
                                        haptic.success()
                                        viewModel.removeFromCart(item.itemType.name)
                                        true
                                    } else false
                                }
                            )

                        SwipeToDismiss(
                            state = dismissState,
                            directions = setOf(DismissDirection.EndToStart),
                            background = {
                                val color =
                                    when (dismissState.targetValue) {
                                        DismissValue.DismissedToStart ->
                                            MaterialTheme.colorScheme.error

                                        else -> Color.Transparent
                                    }
                                Box(
                                    modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .background(color)
                                        .padding(horizontal = 20.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.White
                                    )
                                }
                            },
                            dismissContent = {
                                CartItemCard(
                                    item = item,
                                    onRemove = {
                                        haptic.success()
                                        viewModel.removeFromCart(item.itemType.name)
                                    }
                                )
                            }
                        )
                    }

                    // Suggested Items Section
                    if (state.suggestedItems.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "✨ Complete Your Order",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        items(
                            items = state.suggestedItems,
                            key = { "suggested_${it.itemType}" }) { suggestion ->
                            SuggestedItemCard(
                                suggestion = suggestion,
                                onAdd = {
                                    haptic.click()
                                    viewModel.addSuggestedItem(suggestion.itemType)
                                }
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(100.dp)) }
                }
            }
        }
    }
}

@Composable
private fun EmptyCartView(onNavigateBack: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "🛒", style = MaterialTheme.typography.displayLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Your cart is empty",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Add some items to get started",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onNavigateBack) { Text("Add Items") }
    }
}

@Composable
private fun ServiceTypeCard(serviceType: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Service Type",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text =
                    serviceType.replace("_", " ").lowercase().replaceFirstChar {
                        it.uppercase()
                    },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Icon(
                Icons.Default.LocalLaundryService,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun CartItemCard(item: CartItem, onRemove: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.itemType.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Quantity: ${item.quantity}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "KSh ${item.pricePerItem} × ${item.quantity}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "KSh ${item.pricePerItem * item.quantity}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = onRemove) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remove",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun SuggestedItemCard(suggestion: SuggestedItem, onAdd: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = suggestion.itemType.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )

                    if (suggestion.discountPercent > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        AssistChip(
                            onClick = {},
                            label = { Text("${suggestion.discountPercent}% OFF") },
                            colors =
                            AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                labelColor = MaterialTheme.colorScheme.onError
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = suggestion.reason,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            FilledTonalButton(
                onClick = onAdd,
                colors =
                ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add")
            }
        }
    }
}
