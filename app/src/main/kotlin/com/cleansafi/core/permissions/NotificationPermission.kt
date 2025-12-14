package com.cleansafi.core.permissions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun rememberNotificationPermission(
    onPermissionGranted: () -> Unit = {},
    onPermissionDenied: () -> Unit = {}
): NotificationPermissionState {
    val context = LocalContext.current
    val hasPermission = remember {
        mutableStateOf(context.hasNotificationPermission())
    }
    
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission.value = isGranted
        if (isGranted) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }
    
    return remember {
        NotificationPermissionState(
            hasPermission = hasPermission.value,
            launcher = launcher,
            context = context,
            onPermissionChange = { hasPermission.value = it }
        )
    }
}

class NotificationPermissionState(
    val hasPermission: Boolean,
    private val launcher: ManagedActivityResultLauncher<String, Boolean>,
    private val context: Context,
    private val onPermissionChange: (Boolean) -> Unit
) {
    fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            onPermissionChange(true) // Granted by default on older versions
        }
    }
    
    fun shouldShowRationale(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity,
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
        return false
    }
    
    fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }
}

@Composable
fun NotificationPermissionDialog(
    permissionState: NotificationPermissionState,
    onDismiss: () -> Unit
) {
    var showRationale by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        showRationale = permissionState.shouldShowRationale()
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { androidx.compose.material.icons.Icons.Default.Notifications },
        title = { Text("Enable Notifications") },
        text = {
            Text(
                if (showRationale) {
                    "Notifications are required to keep you updated on your order status. You can enable them in app settings."
                } else {
                    "Get real-time updates on your laundry orders. We'll notify you when your order is picked up, being cleaned, and ready for delivery."
                }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (showRationale) {
                        permissionState.openSettings()
                    } else {
                        permissionState.requestPermission()
                    }
                    onDismiss()
                }
            ) {
                Text(if (showRationale) "Open Settings" else "Allow")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Not Now")
            }
        }
    )
}

private fun Context.hasNotificationPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true // Granted by default on older versions
    }
}
