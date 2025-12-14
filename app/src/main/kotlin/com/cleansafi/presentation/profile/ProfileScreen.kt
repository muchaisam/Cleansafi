package com.cleansafi.presentation.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
        onNavigateBack: () -> Unit,
        onNavigateToLogin: () -> Unit,
        networkMonitor: com.cleansafi.core.network.NetworkMonitor,
        viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isOnline by networkMonitor.isOnline.collectAsStateWithLifecycle(true)
    val haptic = com.cleansafi.core.ui.rememberHapticFeedback()

    Scaffold(
            topBar = {
                TopAppBar(
                        title = { Text("Profile") },
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
        Box(modifier = Modifier.fillMaxSize()) {
            if (state.isLoading) {
                Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
                    com.cleansafi.core.ui.ShimmerProfileCard()
                    Spacer(modifier = Modifier.height(16.dp))
                    com.cleansafi.core.ui.ShimmerEffect(
                            modifier = Modifier.fillMaxWidth().height(100.dp)
                    )
                }
            } else {
                Column(
                        modifier =
                                Modifier.fillMaxSize()
                                        .padding(paddingValues)
                                        .verticalScroll(rememberScrollState())
                                        .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // User Info Card
                    Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors =
                                    CardDefaults.cardColors(
                                            containerColor =
                                                    MaterialTheme.colorScheme.primaryContainer
                                    )
                    ) {
                        Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                    Icons.Default.AccountCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(80.dp),
                                    tint = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            if (state.isEditMode) {
                                OutlinedTextField(
                                        value = state.editName,
                                        onValueChange = viewModel::onNameChange,
                                        label = { Text("Full Name") },
                                        leadingIcon = { Icon(Icons.Default.Person, null) },
                                        modifier = Modifier.fillMaxWidth(),
                                        isError = state.nameError != null
                                )

                                AnimatedVisibility(visible = state.nameError != null) {
                                    Text(
                                            text = state.nameError ?: "",
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                        value = state.editPhoneNumber,
                                        onValueChange = viewModel::onPhoneNumberChange,
                                        label = { Text("Phone Number") },
                                        leadingIcon = { Icon(Icons.Default.Phone, null) },
                                        modifier = Modifier.fillMaxWidth(),
                                        isError = state.phoneError != null
                                )

                                AnimatedVisibility(visible = state.phoneError != null) {
                                    Text(
                                            text = state.phoneError ?: "",
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedButton(
                                            onClick = viewModel::cancelEdit,
                                            modifier = Modifier.weight(1f),
                                            enabled = !state.isSaving
                                    ) { Text("Cancel") }

                                    Button(
                                            onClick = viewModel::saveProfile,
                                            modifier = Modifier.weight(1f),
                                            enabled = !state.isSaving
                                    ) {
                                        if (state.isSaving) {
                                            CircularProgressIndicator(
                                                    modifier = Modifier.size(20.dp),
                                                    color = MaterialTheme.colorScheme.onPrimary
                                            )
                                        } else {
                                            Text("Save")
                                        }
                                    }
                                }
                            } else {
                                Text(
                                        text = state.user?.name ?: "",
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                        text = state.user?.email ?: "",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Text(
                                        text = state.user?.phoneNumber ?: "",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                OutlinedButton(
                                        onClick = viewModel::enterEditMode,
                                        modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                            Icons.Default.Edit,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Edit Profile")
                                }
                            }
                        }
                    }

                    // Stats Card
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                    text = "Your Statistics",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                StatItem(
                                        icon = Icons.Default.ShoppingBag,
                                        label = "Total Orders",
                                        value = state.totalOrders.toString()
                                )

                                Divider(
                                        modifier = Modifier.height(60.dp).width(1.dp),
                                        color = MaterialTheme.colorScheme.outlineVariant
                                )

                                StatItem(
                                        icon = Icons.Default.Payments,
                                        label = "Total Spent",
                                        value = "KSh ${state.totalSpent}"
                                )
                            }
                        }
                    }

                    // Settings Section
                    Text(
                            text = "Settings",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                    )

                    ThemeSettingsItem(viewModel = viewModel)

                    SettingsItem(
                            icon = Icons.Default.Notifications,
                            title = "Notifications",
                            subtitle = "Manage notification preferences",
                            onClick = { /* TODO */}
                    )

                    SettingsItem(
                            icon = Icons.Default.Info,
                            title = "About",
                            subtitle = "App version and information",
                            onClick = { /* TODO */}
                    )

                    SettingsItem(
                            icon = Icons.Default.Help,
                            title = "Help & Support",
                            subtitle = "Get help or contact support",
                            onClick = { /* TODO */}
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Logout Button
                    OutlinedButton(
                            onClick = viewModel::showLogoutDialog,
                            modifier = Modifier.fillMaxWidth(),
                            colors =
                                    ButtonDefaults.outlinedButtonColors(
                                            contentColor = MaterialTheme.colorScheme.error
                                    )
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Logout")
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // App Version
                    Text(
                            text = "CleanSafi v1.0.0 (Compose Edition)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

        // Logout Confirmation Dialog
        if (state.showLogoutDialog) {
            AlertDialog(
                    onDismissRequest = viewModel::hideLogoutDialog,
                    icon = { Icon(Icons.Default.Logout, contentDescription = null) },
                    title = { Text("Logout") },
                    text = { Text("Are you sure you want to logout?") },
                    confirmButton = {
                        Button(
                                onClick = {
                                    haptic.success()
                                    viewModel.logout()
                                    onNavigateToLogin()
                                },
                                colors =
                                        ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.error
                                        )
                        ) { Text("Logout") }
                    },
                    dismissButton = {
                        TextButton(onClick = viewModel::hideLogoutDialog) { Text("Cancel") }
                    }
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            com.cleansafi.core.ui.OfflineIndicator(
                    networkMonitor = networkMonitor,
                    modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun StatItem(
        icon: androidx.compose.ui.graphics.vector.ImageVector,
        label: String,
        value: String
) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsItem(
        icon: androidx.compose.ui.graphics.vector.ImageVector,
        title: String,
        subtitle: String,
        onClick: () -> Unit
) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                )
                Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeSettingsItem(viewModel: ProfileViewModel) {
    val themeManager = viewModel.getThemeManager()

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                    if (themeManager.isDarkTheme) Icons.Default.DarkMode
                    else Icons.Default.LightMode,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                        text = "Theme",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                )
                Text(
                        text = if (themeManager.isDarkTheme) "Dark mode" else "Light mode",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Switch(
                    checked = themeManager.isDarkTheme,
                    onCheckedChange = { themeManager.toggleTheme() }
            )
        }
    }
}
