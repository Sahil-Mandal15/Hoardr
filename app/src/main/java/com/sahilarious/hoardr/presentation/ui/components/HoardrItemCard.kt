package com.sahilarious.hoardr.presentation.ui.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.sahilarious.hoardr.presentation.model.HoardrUiItem
import com.sahilarious.hoardr.presentation.model.ItemCategory
import com.sahilarious.hoardr.presentation.ui.theme.Quicksand
import com.sahilarious.hoardr.presentation.ui.theme.hoardrColors

@Composable
fun HoardrItemCard(
    item: HoardrUiItem,
    modifier: Modifier = Modifier,
    onItemClick: (() -> Unit)? = null,
    onMarkAsReadClick: (() -> Unit)? = null,
    onDeleteClick: (() -> Unit)? = null,
    onPriorityClick: (() -> Unit)? = null,
    onMoreClick: (() -> Unit)? = null,
) {
    val colors = MaterialTheme.hoardrColors
    var isMenuExpanded by remember { mutableStateOf(false) }

    Card(
        onClick = { onItemClick?.invoke() },
        enabled = onItemClick != null,
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

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CategoryBadge(category = item.category)

                    Box {
                        IconButton(
                            onClick = {
                                onMoreClick?.invoke()
                                isMenuExpanded = true
                            },
                            modifier = Modifier.size(24.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.MoreHoriz,
                                contentDescription = "More options",
                                tint = colors.textTertiary,
                            )
                        }

                        DropdownMenu(
                            expanded = isMenuExpanded,
                            onDismissRequest = { isMenuExpanded = false },
                            modifier = Modifier.background(colors.surface),
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Priority",
                                        fontFamily = Quicksand,
                                        fontSize = 14.sp,
                                        color = colors.textPrimary,
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (item.isPriority) Icons.Filled.Star else Icons.Outlined.Star,
                                        contentDescription = "Priority",
                                        tint = if (item.isPriority) colors.accent else colors.textSecondary,
                                        modifier = Modifier.size(18.dp),
                                    )
                                },
                                onClick = {
                                    isMenuExpanded = false
                                    onPriorityClick?.invoke()
                                },
                            )

                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Mark as read",
                                        fontFamily = Quicksand,
                                        fontSize = 14.sp,
                                        color = colors.textPrimary,
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.CheckCircle,
                                        contentDescription = "Mark as read",
                                        tint = colors.textSecondary,
                                        modifier = Modifier.size(18.dp),
                                    )
                                },
                                onClick = {
                                    isMenuExpanded = false
                                    onMarkAsReadClick?.invoke()
                                },
                            )

                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Delete",
                                        fontFamily = Quicksand,
                                        fontSize = 14.sp,
                                        color = colors.error,
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.Delete,
                                        contentDescription = "Delete",
                                        tint = colors.error,
                                        modifier = Modifier.size(18.dp),
                                    )
                                },
                                onClick = {
                                    isMenuExpanded = false
                                    onDeleteClick?.invoke()
                                },
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

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

                if (!item.description.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.description,
                        fontFamily = Quicksand,
                        fontSize = 12.sp,
                        color = colors.textSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 16.sp,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val icon = when (item.category) {
                        ItemCategory.ARTICLE -> Icons.Outlined.Link
                        ItemCategory.NOTE -> Icons.Outlined.Description
                        ItemCategory.IDEA -> Icons.Outlined.Lightbulb
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
    category: ItemCategory,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.hoardrColors

    val (bgColor, textColor, label) = when (category) {
        ItemCategory.ARTICLE -> Triple(
            colors.secondaryContainer,
            colors.secondary,
            "Article",
        )
        ItemCategory.NOTE -> Triple(
            Color(0xFF1B3328),
            colors.success,
            "Note",
        )
        ItemCategory.IDEA -> Triple(
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
