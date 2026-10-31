package com.sahilarious.hoardr.presentation.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import com.sahilarious.hoardr.presentation.model.HoardrUiItem
import com.sahilarious.hoardr.presentation.ui.components.CategoryBadge
import com.sahilarious.hoardr.presentation.ui.components.HoardrHeaderLogo
import com.sahilarious.hoardr.presentation.ui.theme.Caveat
import com.sahilarious.hoardr.presentation.ui.theme.Quicksand
import com.sahilarious.hoardr.presentation.ui.theme.hoardrColors
import com.sahilarious.hoardr.presentation.viewmodel.ExploreViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier,
    viewModel: ExploreViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val colors = MaterialTheme.hoardrColors

    val articles = uiState.articles

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Top Header Bar with HoardrLogo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HoardrHeaderLogo()

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(colors.surfaceVariant)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
            ) {
                Text(
                    text = "${articles.size} left",
                    fontFamily = Quicksand,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = colors.primary,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
        ) {
            Text(
                text = "Swipe right to keep • Swipe left to delete",
                fontFamily = Quicksand,
                fontSize = 13.sp,
                color = colors.textSecondary,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Card Stack Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            if (articles.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "🚀",
                        fontSize = 52.sp,
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "All caught up!",
                        fontFamily = Caveat,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "No unread articles left in your hoard.",
                        fontFamily = Quicksand,
                        fontSize = 14.sp,
                        color = colors.textSecondary,
                    )
                }
            } else {
                val visibleArticles = articles.take(3).reversed()
                visibleArticles.forEachIndexed { index, item ->
                    key(item.id) {
                        val isTopCard = index == visibleArticles.lastIndex
                        SwipeableArticleCard(
                            item = item,
                            isTopCard = isTopCard,
                            onSwipeLeft = {
                                viewModel.deleteArticle(item)
                            },
                            onSwipeRight = {
                                viewModel.dismissArticle(item)
                            },
                            onCardClick = {
                                val rawUrl = item.rawUrl?.trim()
                                if (!rawUrl.isNullOrBlank()) {
                                    val formattedUrl = if (!rawUrl.startsWith("http://") && !rawUrl.startsWith("https://")) {
                                        "https://$rawUrl"
                                    } else {
                                        rawUrl
                                    }
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(formattedUrl))
                                        val chooser = Intent.createChooser(intent, "Open with")
                                        context.startActivity(chooser)
                                    } catch (_: Exception) {
                                        Toast.makeText(context, "Unable to open link", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Button
        if (articles.isNotEmpty()) {
            val topItem = articles.first()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = {
                        viewModel.markAsRead(topItem)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary,
                        contentColor = colors.onPrimary,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = null,
                        tint = colors.onPrimary,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Mark as Read",
                        fontFamily = Quicksand,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                    )
                }

                IconButton(
                    onClick = {
                        viewModel.togglePriority(topItem)
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .background(colors.surfaceVariant, RoundedCornerShape(25.dp)),
                ) {
                    Icon(
                        imageVector = if (topItem.isPriority) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = "Priority",
                        tint = if (topItem.isPriority) colors.accent else colors.textPrimary,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun SwipeableArticleCard(
    item: HoardrUiItem,
    isTopCard: Boolean,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    onCardClick: () -> Unit,
) {
    val colors = MaterialTheme.hoardrColors
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val swipeThreshold = 140f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(440.dp)
            .padding(horizontal = 4.dp)
            .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
            .rotate(rotation.value)
            .then(
                if (isTopCard) {
                    Modifier.pointerInput(Unit) {
                        detectDragGestures(
                            onDragEnd = {
                                coroutineScope.launch {
                                    if (offsetX.value > swipeThreshold) {
                                        offsetX.animateTo(1000f, animationSpec = spring(dampingRatio = 0.75f, stiffness = 250f))
                                        onSwipeRight()
                                    } else if (offsetX.value < -swipeThreshold) {
                                        offsetX.animateTo(-1000f, animationSpec = spring(dampingRatio = 0.75f, stiffness = 250f))
                                        onSwipeLeft()
                                    } else {
                                        launch { offsetX.animateTo(0f, animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f)) }
                                        launch { offsetY.animateTo(0f, animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f)) }
                                        launch { rotation.animateTo(0f, animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f)) }
                                    }
                                }
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                coroutineScope.launch {
                                    offsetX.snapTo(offsetX.value + dragAmount.x)
                                    offsetY.snapTo(offsetY.value + dragAmount.y)
                                    rotation.snapTo((offsetX.value / 16).coerceIn(-25f, 25f))
                                }
                            },
                        )
                    }
                } else {
                    Modifier
                }
            )
            .clickable { onCardClick() },
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CategoryBadge(category = item.category)

                Text(
                    text = item.timeAgo,
                    fontFamily = Quicksand,
                    fontSize = 12.sp,
                    color = colors.textTertiary,
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Article Image
            SubcomposeAsyncImage(
                model = item.imageUrl,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(RoundedCornerShape(20.dp)),
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

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = item.title,
                fontFamily = Quicksand,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = colors.textPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 22.sp,
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

            Spacer(modifier = Modifier.weight(1f))

            // Source Domain
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Link,
                    contentDescription = null,
                    tint = colors.textSecondary,
                    modifier = Modifier.size(14.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = item.sourceOrFolder,
                    fontFamily = Quicksand,
                    fontSize = 12.sp,
                    color = colors.textSecondary,
                )
            }
        }
    }
}
