package com.sahilarious.hoardr.presentation.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.sahilarious.hoardr.presentation.ui.theme.Caveat
import com.sahilarious.hoardr.presentation.ui.theme.HoardrTheme
import com.sahilarious.hoardr.presentation.ui.theme.Quicksand
import com.sahilarious.hoardr.presentation.ui.theme.hoardrColors

enum class CategoryType {
    ARTICLE, NOTE, IDEA
}

data class HoardrItem(
    val id: Long,
    val title: String,
    val category: CategoryType,
    val sourceOrFolder: String,
    val timeAgo: String,
    val imageUrl: String,
)

val sampleHoardrItems = listOf(
    HoardrItem(
        id = 1,
        title = "The Psychology of Better Habits",
        category = CategoryType.ARTICLE,
        sourceOrFolder = "medium.com",
        timeAgo = "2 days ago",
        imageUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb",
    ),
    HoardrItem(
        id = 2,
        title = "Android ViewModel – Quick Notes",
        category = CategoryType.NOTE,
        sourceOrFolder = "My Notes",
        timeAgo = "3 days ago",
        imageUrl = "https://images.unsplash.com/photo-1555066931-4365d14bab8c",
    ),
    HoardrItem(
        id = 3,
        title = "Build a minimal habit tracker app",
        category = CategoryType.IDEA,
        sourceOrFolder = "Personal Ideas",
        timeAgo = "5 days ago",
        imageUrl = "https://images.unsplash.com/photo-1517842645767-c639042777db",
    ),
    HoardrItem(
        id = 4,
        title = "Shutter Island – Why it still hits different",
        category = CategoryType.ARTICLE,
        sourceOrFolder = "theatlantic.com",
        timeAgo = "6 days ago",
        imageUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23",
    ),
    HoardrItem(
        id = 5,
        title = "Kolkata places to visit this winter",
        category = CategoryType.NOTE,
        sourceOrFolder = "Travel Plans",
        timeAgo = "1 week ago",
        imageUrl = "https://images.unsplash.com/photo-1513694203232-719a280e022f",
    ),
)

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
fun HomeScreen(
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.hoardrColors
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedNavIndex by remember { mutableIntStateOf(0) }

    val categories = listOf("All", "Articles", "Notes", "Ideas")

    val filteredItems = remember(selectedCategory) {
        when (selectedCategory) {
            "Articles" -> sampleHoardrItems.filter { it.category == CategoryType.ARTICLE }
            "Notes" -> sampleHoardrItems.filter { it.category == CategoryType.NOTE }
            "Ideas" -> sampleHoardrItems.filter { it.category == CategoryType.IDEA }
            else -> sampleHoardrItems
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        bottomBar = {
            NavigationBar(
                containerColor = colors.surface,
                contentColor = colors.textPrimary,
                tonalElevation = 8.dp,
            ) {
                bottomNavItems.forEachIndexed { index, item ->
                    val isSelected = selectedNavIndex == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedNavIndex = index },
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
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Top Header: Logo + Profile Icon
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

            // Greeting Section
            Text(
                text = "Greetings, Anon 🚀",
                fontFamily = Caveat,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Here are your saved articles, notes and ideas.",
                fontFamily = Quicksand,
                fontSize = 14.sp,
                color = colors.textSecondary,
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Category Filter Chips Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                items(categories) { category ->
                    val isSelected = category == selectedCategory
                    val chipBg = if (isSelected) colors.primary else colors.surfaceVariant
                    val chipTextColor = if (isSelected) colors.textPrimary else colors.textSecondary

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(chipBg)
                            .clickable { selectedCategory = category }
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = category,
                            fontFamily = Quicksand,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                            fontSize = 14.sp,
                            color = chipTextColor,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Saved Items List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                items(filteredItems, key = { it.id }) { item ->
                    HoardrItemCard(item = item)
                }
            }
        }
    }
}

@Composable
fun HoardrHeaderLogo(
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.hoardrColors

    Box(
        modifier = modifier
            .width(110.dp)
            .height(48.dp)
            .rotate(-4f),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = "Hoardr",
            fontFamily = Caveat,
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary,
            letterSpacing = (-1.0).sp,
        )

        Canvas(
            modifier = Modifier.fillMaxSize(),
        ) {
            val path = Path().apply {
                moveTo(
                    x = size.width * 0.05f,
                    y = size.height * 0.82f,
                )
                cubicTo(
                    x1 = size.width * 0.35f,
                    y1 = size.height * 0.79f,
                    x2 = size.width * 0.65f,
                    y2 = size.height * 0.76f,
                    x3 = size.width * 0.85f,
                    y3 = size.height * 0.72f,
                )
            }

            drawPath(
                path = path,
                color = colors.primary,
                style = Stroke(
                    width = 2.8f.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                ),
            )
        }
    }
}

@Composable
fun HoardrItemCard(
    item: HoardrItem,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.hoardrColors

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Left Thumbnail
            SubcomposeAsyncImage(
                model = item.imageUrl,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(86.dp)
                    .clip(RoundedCornerShape(14.dp)),
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colors.surfaceVariant),
                    )
                },
                error = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colors.surfaceVariant),
                    )
                },
            )

            Spacer(modifier = Modifier.width(14.dp))

            // Right Content Details
            Column(
                modifier = Modifier.weight(1f),
            ) {
                // Category Tag + Options Menu
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CategoryBadge(category = item.category)

                    IconButton(
                        onClick = { },
                        modifier = Modifier.size(24.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.MoreHoriz,
                            contentDescription = "More options",
                            tint = colors.textTertiary,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Title
                Text(
                    text = item.title,
                    fontFamily = Quicksand,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = colors.textPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp,
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Source / Folder + Icon + Timestamp
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val icon = when (item.category) {
                        CategoryType.ARTICLE -> Icons.Outlined.Link
                        CategoryType.NOTE -> Icons.Outlined.Description
                        CategoryType.IDEA -> Icons.Outlined.Lightbulb
                    }

                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = colors.textSecondary,
                        modifier = Modifier.size(14.dp),
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = item.sourceOrFolder,
                        fontFamily = Quicksand,
                        fontSize = 12.sp,
                        color = colors.textSecondary,
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "•",
                        fontFamily = Quicksand,
                        fontSize = 12.sp,
                        color = colors.textTertiary,
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = item.timeAgo,
                        fontFamily = Quicksand,
                        fontSize = 12.sp,
                        color = colors.textTertiary,
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryBadge(
    category: CategoryType,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.hoardrColors

    val (bgColor, textColor, label) = when (category) {
        CategoryType.ARTICLE -> Triple(
            colors.secondaryContainer,
            colors.secondary,
            "Article",
        )
        CategoryType.NOTE -> Triple(
            Color(0xFF1B3328),
            colors.success,
            "Note",
        )
        CategoryType.IDEA -> Triple(
            Color(0xFF382A1B),
            colors.accent,
            "Idea",
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            fontFamily = Quicksand,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            color = textColor,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    HoardrTheme(darkTheme = true) {
        HomeScreen()
    }
}
