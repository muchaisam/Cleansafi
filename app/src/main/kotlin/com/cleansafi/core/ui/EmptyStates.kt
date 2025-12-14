package com.cleansafi.core.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun EmptyStateView(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    message: String,
    actionText: String? = null,
    onAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        if (actionText != null && onAction != null) {
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = onAction,
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text(actionText)
            }
        }
    }
}

@Composable
fun EmptyOrdersState(
    onPlaceOrder: () -> Unit,
    modifier: Modifier = Modifier
) {
    EmptyStateView(
        icon = Icons.Default.ShoppingBag,
        title = "No Orders Yet",
        message = "Start your first laundry order today and experience the convenience of CleanSafi!",
        actionText = "Place Your First Order",
        onAction = onPlaceOrder,
        modifier = modifier
    )
}

@Composable
fun EmptyCartState(
    onAddItems: () -> Unit,
    modifier: Modifier = Modifier
) {
    EmptyStateView(
        icon = Icons.Default.ShoppingCart,
        title = "Your Cart is Empty",
        message = "Add some items to your cart to get started with your laundry order.",
        actionText = "Add Items",
        onAction = onAddItems,
        modifier = modifier
    )
}

@Composable
fun EmptySearchState(
    modifier: Modifier = Modifier
) {
    EmptyStateView(
        icon = Icons.Default.SearchOff,
        title = "No Results Found",
        message = "Try adjusting your search or filters to find what you're looking for.",
        modifier = modifier
    )
}
