package com.cleansafi.presentation.order.placeorder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cleansafi.domain.model.LaundryItemType
import com.cleansafi.domain.model.ServiceType
import androidx.compose.ui.layout.ContentScale
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceOrderScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCart: () -> Unit,
    networkMonitor: com.cleansafi.core.network.NetworkMonitor,
    viewModel: PlaceOrderViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isOnline by networkMonitor.isOnline.collectAsStateWithLifecycle(true)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Place Order") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            if (state.hasItems) {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Column(
                        modifier = Modifier
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
                                    text = "Total: KSh ${state.totalPrice}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${state.totalItems} items",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Button(
                                onClick = {
                                    viewModel.addToCart()
                                    onNavigateToCart()
                                },
                                modifier = Modifier.height(48.dp)
                            ) {
                                Icon(
                                    Icons.Default.ShoppingCart,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Add to Cart")
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Service Type Selection
                Text(
                    text = "Select Service",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                ServiceTypeSelector(
                    selectedService = state.serviceType,
                    onServiceSelected = viewModel::onServiceTypeChange
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Item Selection
                Text(
                    text = "Select Items",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                LaundryItemType.entries.forEach { itemType ->
                    LaundryItemCard(
                        itemType = itemType,
                        quantity = state.itemQuantities[itemType] ?: 0,
                        pricePerItem = PricingCalculator.calculateItemPrice(
                            itemType,
                            state.serviceType
                        ),
                        onIncrement = { viewModel.incrementQuantity(itemType) },
                        onDecrement = { viewModel.decrementQuantity(itemType) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }

                Spacer(modifier = Modifier.height(100.dp))
            }

        com.cleansafi.core.ui.OfflineIndicator(
            networkMonitor = networkMonitor,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun ServiceTypeSelector(
        selectedService: ServiceType,
        onServiceSelected: (ServiceType) -> Unit
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ServiceType.entries.forEach { serviceType ->
                ServiceTypeCard(
                    serviceType = serviceType,
                    isSelected = selectedService == serviceType,
                    onSelect = { onServiceSelected(serviceType) }
                )
            }
        }
    }
}

@Composable
private fun ServiceTypeCard(
        serviceType: ServiceType,
        isSelected: Boolean,
        onSelect: () -> Unit
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = onSelect,
            colors = CardDefaults.cardColors(
                containerColor = if (isSelected)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.surface
            ),
            border = if (isSelected)
                CardDefaults.outlinedCardBorder()
            else null
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
                        text = when (serviceType) {
                            ServiceType.WASH_ONLY -> "Wash Only"
                            ServiceType.IRON_ONLY -> "Iron Only"
                            ServiceType.WASH_AND_IRON -> "Wash & Iron"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                    Text(
                        text = when (serviceType) {
                            ServiceType.WASH_ONLY -> "Professional washing service"
                            ServiceType.IRON_ONLY -> "Expert ironing service"
                            ServiceType.WASH_AND_IRON -> "Complete laundry service"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                RadioButton(
                    selected = isSelected,
                    onClick = onSelect
                )
            }
        }
    }
}

@Composable
private fun LaundryItemCard(
        itemType: LaundryItemType,
        quantity: Int,
        pricePerItem: Int,
        onIncrement: () -> Unit,
        onDecrement: () -> Unit
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (quantity > 0)
                    MaterialTheme.colorScheme.secondaryContainer
                else
                    MaterialTheme.colorScheme.surface
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
                    Text(
                        text = itemType.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "KSh $pricePerItem per item",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    AnimatedVisibility(visible = quantity > 0) {
                        Text(
                            text = "Subtotal: KSh ${pricePerItem * quantity}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = onDecrement,
                        enabled = quantity > 0
                    ) {
                        Icon(
                            Icons.Default.Remove,
                            contentDescription = "Decrease",
                            tint = if (quantity > 0)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        )
                    }

                    Text(
                        text = quantity.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.widthIn(min = 32.dp)
                    )

                    IconButton(onClick = onIncrement) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Increase",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
}
