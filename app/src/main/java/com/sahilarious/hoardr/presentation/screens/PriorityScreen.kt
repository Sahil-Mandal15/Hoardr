package com.sahilarious.hoardr.presentation.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sahilarious.hoardr.presentation.model.HoardrUiItem
import com.sahilarious.hoardr.presentation.ui.components.HoardrHeaderLogo
import com.sahilarious.hoardr.presentation.ui.components.HoardrItemCard
import com.sahilarious.hoardr.presentation.ui.theme.Caveat
import com.sahilarious.hoardr.presentation.ui.theme.Quicksand
import com.sahilarious.hoardr.presentation.ui.theme.hoardrColors
import com.sahilarious.hoardr.presentation.viewmodel.PriorityUiState
import com.sahilarious.hoardr.presentation.viewmodel.PriorityViewModel

@Composable
fun PriorityScreen(
    modifier: Modifier = Modifier,
    viewModel: PriorityViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    PriorityScreenContent(
        uiState = uiState,
        onCategorySelected = viewModel::onCategorySelected,
        onDeleteItem = viewModel::deleteItem,
        onMarkAsRead = viewModel::markAsRead,
        onPriorityClick = viewModel::togglePriority,
        modifier = modifier,
    )
}

@Composable
fun PriorityScreenContent(
    uiState: PriorityUiState,
    onCategorySelected: (String) -> Unit,
    onDeleteItem: (HoardrUiItem) -> Unit,
    onMarkAsRead: (HoardrUiItem) -> Unit,
    onPriorityClick: (HoardrUiItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val colors = MaterialTheme.hoardrColors
    val categories = listOf("All", "Articles", "Notes", "Ideas")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Top Header Bar
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

        // Greeting Section / Title
        Text(
            text = "Priority Hoards ⭐",
            fontFamily = Caveat,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Here are your starred priority items.",
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
                val isSelected = category == uiState.selectedCategory
                val chipBg = if (isSelected) colors.primary else colors.surfaceVariant
                val chipTextColor = if (isSelected) colors.textPrimary else colors.textSecondary

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(chipBg)
                        .clickable { onCategorySelected(category) }
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

        // Priority Items List or Empty State
        if (uiState.items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "No priority items yet.",
                    fontFamily = Quicksand,
                    fontSize = 15.sp,
                    color = colors.textTertiary,
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                items(uiState.items, key = { it.id }) { item ->
                    HoardrItemCard(
                        item = item,
                        onItemClick = {
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
                        onMarkAsReadClick = {
                            onMarkAsRead(item)
                        },
                        onDeleteClick = {
                            onDeleteItem(item)
                        },
                        onPriorityClick = {
                            onPriorityClick(item)
                        },
                    )
                }
            }
        }
    }
}
