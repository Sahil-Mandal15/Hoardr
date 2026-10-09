package com.sahilarious.hoardr.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sahilarious.hoardr.presentation.ui.theme.Quicksand
import com.sahilarious.hoardr.presentation.ui.theme.hoardrColors

data class BottomNavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

val bottomNavItems = listOf(
    BottomNavItem("Home", Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavItem("Explore", Icons.Outlined.Explore, Icons.Outlined.Explore),
    BottomNavItem("Saved", Icons.Outlined.BookmarkBorder, Icons.Outlined.BookmarkBorder),
    BottomNavItem("Profile", Icons.Outlined.Person, Icons.Outlined.PersonOutline),
)

@Composable
fun HomeBottomNavBar(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.hoardrColors

    NavigationBar(
        modifier = modifier,
        containerColor = colors.surface,
        contentColor = colors.textPrimary,
        tonalElevation = 8.dp,
    ) {
        bottomNavItems.forEachIndexed { index, item ->
            val isSelected = selectedIndex == index
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontFamily = Quicksand,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        fontSize = 12.sp,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colors.primary,
                    selectedTextColor = colors.primary,
                    unselectedIconColor = colors.textTertiary,
                    unselectedTextColor = colors.textTertiary,
                    indicatorColor = Color.Transparent,
                ),
            )
        }
    }
}
