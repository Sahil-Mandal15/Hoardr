package com.sahilarious.hoardr.presentation.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SettingsSystemDaydream
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sahilarious.hoardr.data.preferences.OnboardingManager
import com.sahilarious.hoardr.presentation.ui.components.HoardrHeaderLogo
import com.sahilarious.hoardr.presentation.ui.theme.Caveat
import com.sahilarious.hoardr.presentation.ui.theme.Quicksand
import com.sahilarious.hoardr.presentation.ui.theme.hoardrColors

@Composable
fun ProfileScreen(
    onboardingManager: OnboardingManager,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.hoardrColors
    val currentTheme = onboardingManager.themeMode

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HoardrHeaderLogo()

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colors.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Profile",
                    tint = colors.textPrimary,
                    modifier = Modifier.size(22.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Profile & Settings ⚙️",
            fontFamily = Caveat,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Manage your preferences and appearance.",
            fontFamily = Quicksand,
            fontSize = 14.sp,
            color = colors.textSecondary,
        )

        Spacer(modifier = Modifier.height(24.dp))

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
