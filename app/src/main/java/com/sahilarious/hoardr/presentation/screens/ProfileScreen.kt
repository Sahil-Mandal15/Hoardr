package com.sahilarious.hoardr.presentation.screens

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.SettingsSystemDaydream
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.imageLoader
import com.sahilarious.hoardr.data.preferences.OnboardingManager
import com.sahilarious.hoardr.presentation.ui.components.HoardrHeaderLogo
import com.sahilarious.hoardr.presentation.ui.theme.Caveat
import com.sahilarious.hoardr.presentation.ui.theme.Quicksand
import com.sahilarious.hoardr.presentation.ui.theme.hoardrColors
import com.sahilarious.hoardr.presentation.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun ProfileScreen(
    onboardingManager: OnboardingManager,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val colors = MaterialTheme.hoardrColors
    val currentTheme = onboardingManager.themeMode
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val jsonString = inputStream?.bufferedReader().use { reader -> reader?.readText() }
                if (!jsonString.isNullOrBlank()) {
                    viewModel.importFromJson(jsonString)
                    Toast.makeText(context, "Backup imported successfully!", Toast.LENGTH_SHORT).show()
                }
            } catch (_: Exception) {
                Toast.makeText(context, "Failed to import backup", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp),
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HoardrHeaderLogo()
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Profile & Statistics ⚙️",
            fontFamily = Caveat,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Your hoard overview and reading backlog.",
            fontFamily = Quicksand,
            fontSize = 14.sp,
            color = colors.textSecondary,
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = colors.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Reading Progress",
                        fontFamily = Quicksand,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = colors.textPrimary,
                    )
                    Text(
                        text = "${uiState.readPercentage}% Read",
                        fontFamily = Quicksand,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = colors.primary,
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LinearProgressIndicator(
                    progress = { uiState.readPercentage / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = colors.primary,
                    trackColor = colors.surfaceVariant,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    StatBox(
                        title = "Total",
                        value = "${uiState.totalHoards}",
                        icon = Icons.Outlined.Bookmark,
                        modifier = Modifier.weight(1f),
                    )
                    StatBox(
                        title = "Unread",
                        value = "${uiState.unreadCount}",
                        icon = Icons.Outlined.Inbox,
                        modifier = Modifier.weight(1f),
                    )
                    StatBox(
                        title = "Priority",
                        value = "${uiState.priorityCount}",
                        icon = Icons.Outlined.Star,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Categories",
            fontFamily = Quicksand,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = colors.textPrimary,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            CategoryCountCard(
                title = "Articles",
                count = "${uiState.articleCount}",
                modifier = Modifier.weight(1f),
            )
            CategoryCountCard(
                title = "Notes",
                count = "${uiState.noteCount}",
                modifier = Modifier.weight(1f),
            )
            CategoryCountCard(
                title = "Ideas",
                count = "${uiState.ideaCount}",
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Storage & Data",
            fontFamily = Quicksand,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = colors.textPrimary,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            ActionOptionCard(
                title = "Export Backup (JSON)",
                icon = Icons.Outlined.FileDownload,
                onClick = {
                    coroutineScope.launch {
                        try {
                            val json = viewModel.exportJsonString()
                            val file = File(context.cacheDir, "hoardr_backup_${System.currentTimeMillis()}.json")
                            file.writeText(json)
                            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "application/json"
                                putExtra(Intent.EXTRA_STREAM, uri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            val chooser = Intent.createChooser(intent, "Export Hoardr Backup")
                            context.startActivity(chooser)
                        } catch (_: Exception) {
                            Toast.makeText(context, "Export failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
            )

            ActionOptionCard(
                title = "Import Backup (JSON)",
                icon = Icons.Outlined.FileUpload,
                onClick = {
                    importLauncher.launch(arrayOf("application/json", "*/*"))
                },
            )

            ActionOptionCard(
                title = "Clear Image Cache",
                icon = Icons.Outlined.DeleteSweep,
                onClick = {
                    try {
                        context.cacheDir.deleteRecursively()
                        context.imageLoader.diskCache?.clear()
                        context.imageLoader.memoryCache?.clear()
                        Toast.makeText(context, "Image cache cleared", Toast.LENGTH_SHORT).show()
                    } catch (_: Exception) {
                        Toast.makeText(context, "Failed to clear cache", Toast.LENGTH_SHORT).show()
                    }
                },
            )

            ActionOptionCard(
                title = "Clear Read Items",
                icon = Icons.Outlined.DeleteSweep,
                onClick = {
                    viewModel.clearReadItems()
                    Toast.makeText(context, "Read items cleared", Toast.LENGTH_SHORT).show()
                },
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Appearance",
            fontFamily = Quicksand,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = colors.textPrimary,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            ThemeOptionCard(
                title = "System Default",
                icon = Icons.Outlined.SettingsSystemDaydream,
                isSelected = currentTheme == OnboardingManager.THEME_SYSTEM,
                onClick = { onboardingManager.themeMode = OnboardingManager.THEME_SYSTEM },
            )

            ThemeOptionCard(
                title = "Light Mode",
                icon = Icons.Outlined.LightMode,
                isSelected = currentTheme == OnboardingManager.THEME_LIGHT,
                onClick = { onboardingManager.themeMode = OnboardingManager.THEME_LIGHT },
            )

            ThemeOptionCard(
                title = "Dark Mode",
                icon = Icons.Outlined.DarkMode,
                isSelected = currentTheme == OnboardingManager.THEME_DARK,
                onClick = { onboardingManager.themeMode = OnboardingManager.THEME_DARK },
            )
        }
    }
}

@Composable
fun StatBox(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.hoardrColors

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = colors.primary,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontFamily = Quicksand,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = colors.textPrimary,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                fontFamily = Quicksand,
                fontSize = 11.sp,
                color = colors.textSecondary,
            )
        }
    }
}

@Composable
fun CategoryCountCard(
    title: String,
    count: String,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.hoardrColors

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = count,
                fontFamily = Quicksand,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = colors.textPrimary,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                fontFamily = Quicksand,
                fontSize = 12.sp,
                color = colors.textSecondary,
            )
        }
    }
}

@Composable
fun ActionOptionCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    val colors = MaterialTheme.hoardrColors

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = colors.textPrimary,
                modifier = Modifier.size(24.dp),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                fontFamily = Quicksand,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                color = colors.textPrimary,
            )
        }
    }
}

@Composable
fun ThemeOptionCard(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val colors = MaterialTheme.hoardrColors
    val containerColor = if (isSelected) colors.surfaceVariant else colors.surface
    val contentColor = if (isSelected) colors.primary else colors.textPrimary

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = contentColor,
                modifier = Modifier.size(24.dp),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                fontFamily = Quicksand,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                fontSize = 15.sp,
                color = contentColor,
            )
        }
    }
}
