package com.cleansafi.presentation.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onNavigateToPlaceOrder: () -> Unit,
    onNavigateToOrders: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToAdmin: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
    networkMonitor: com.cleansafi.core.network.NetworkMonitor = androidx.compose.ui.platform.LocalContext.current.let {
        remember { com.cleansafi.core.network.NetworkMonitor(it) }
    }
) {
    val isDemoMode by viewModel.isDemoMode.collectAsStateWithLifecycle()
    var showDemoDialog by remember { mutableStateOf(false) }
    var showPermissionDialog by remember { mutableStateOf(false) }
    val permissionState = com.cleansafi.core.permissions.rememberNotificationPermission(
        onPermissionGranted = { showPermissionDialog = false }
    )

    LaunchedEffect(Unit) {
        if (!permissionState.hasPermission) {
            kotlinx.coroutines.delay(2000) // Show after 2 seconds on home screen
            showPermissionDialog = true
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.combinedClickable(
                            onClick = {},
                            onLongClick = { showDemoDialog = true }
                        )
                    ) {
                        Text("CleanSafi")
                        if (isDemoMode) {
                            Spacer(modifier = Modifier.width(8.dp))
                            AssistChip(
                                onClick = { showDemoDialog = true },
                                label = {
                                    Text(
                                        "DEMO",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    labelColor = MaterialTheme.colorScheme.onError
                                ),
                                modifier = Modifier.height(24.dp)
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToPlaceOrder,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("New Order") },
                containerColor = MaterialTheme.colorScheme.primary
            )
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
                // Welcome Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Welcome!",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Your laundry, simplified",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "🧺",
                            style = MaterialTheme.typography.displayMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Quick Actions
                Text(
                    text = "Quick Actions",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionCard(
                        icon = Icons.Default.LocalLaundryService,
                        title = "New Order",
                        description = "Place a new laundry order",
                        onClick = onNavigateToPlaceOrder,
                        modifier = Modifier.weight(1f)
                    )

                    QuickActionCard(
                        icon = Icons.Default.History,
                        title = "My Orders",
                        description = "View order history",
                        onClick = onNavigateToOrders,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Services Info
                Text(
                    text = "Our Services",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                ServiceInfoCard(
                    title = "Wash Only",
                    description = "Professional washing service for all your clothes",
                    icon = "💧"
                )

                Spacer(modifier = Modifier.height(8.dp))

                ServiceInfoCard(
                    title = "Iron Only",
                    description = "Expert ironing to make your clothes crisp and neat",
                    icon = "🔥"
                )

                Spacer(modifier = Modifier.height(8.dp))

                ServiceInfoCard(
                    title = "Wash & Iron",
                    description = "Complete laundry service - we do it all!",
                    icon = "✨"
                )

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
private fun QuickActionCard(
        icon: androidx.compose.ui.graphics.vector.ImageVector,
        title: String,
        description: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Card(
            onClick = onClick,
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// This dialog should be back in HomeScreen but placing here for now
/*
// Demo Mode Dialog (Hidden easter egg - long press logo)
if (showDemoDialog) {
            AlertDialog(
                onDismissRequest = { showDemoDialog = false },
                icon = { Icon(Icons.Default.DeveloperMode, contentDescription = null) },
                title = { Text("Developer Mode") },
                text = {
                    Text(
                        if (isDemoMode) {
                            "Demo mode is ENABLED\n\nOrder progression is sped up to 5 seconds per stage for quick demonstrations during interviews."
                        } else {
                            "Demo mode is DISABLED\n\nOrders progress with realistic timing (1-3 minutes per stage) for authentic portfolio experience."
                        }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.toggleDemoMode()
                            showDemoDialog = false
                        }
                    ) {
                        Text(if (isDemoMode) "Disable Demo Mode" else "Enable Demo Mode")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDemoDialog = false }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}

// Permission dialog
if (showPermissionDialog) {
    com.cleansafi.core.permissions.NotificationPermissionDialog(
        permissionState = permissionState,
        onDismiss = { showPermissionDialog = false }
    )
}
*/

@Composable
fun ServiceInfoCard(
    title: String,
    description: String,
    icon: String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(end = 16.dp)
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
