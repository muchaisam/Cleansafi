package com.cleansafi.core.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ErrorView(
    error: AppError,
    onRetry: () -> Unit,
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
            imageVector = when (error) {
                is AppError.NetworkError -> Icons.Default.WifiOff
                is AppError.DatabaseError -> Icons.Default.Storage
                is AppError.ValidationError -> Icons.Default.Warning
                is AppError.AuthenticationError -> Icons.Default.Lock
                is AppError.PaymentError -> Icons.Default.Payment
                is AppError.ServerError -> Icons.Default.CloudOff
                else -> Icons.Default.Error
            },
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = when (error) {
                is AppError.NetworkError -> "No Internet Connection"
                is AppError.DatabaseError -> "Database Error"
                is AppError.ValidationError -> "Invalid Input"
                is AppError.AuthenticationError -> "Authentication Failed"
                is AppError.PaymentError -> "Payment Failed"
                is AppError.ServerError -> "Server Error"
                else -> "Something Went Wrong"
            },
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = error.message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onRetry,
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Try Again")
        }
    }
}

@Composable
fun ErrorSnackbar(
    error: AppError,
    onDismiss: () -> Unit,
    onRetry: (() -> Unit)? = null
) {
    Snackbar(
        modifier = Modifier.padding(16.dp),
        action = {
            if (onRetry != null) {
                TextButton(onClick = onRetry) {
                    Text("Retry")
                }
            } else {
                TextButton(onClick = onDismiss) {
                    Text("Dismiss")
                }
            }
        },
        dismissAction = {
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Dismiss")
            }
        }
    ) {
        Text(error.message)
    }
}

@Composable
fun InlineErrorMessage(
    error: AppError,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Error,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = error.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}
