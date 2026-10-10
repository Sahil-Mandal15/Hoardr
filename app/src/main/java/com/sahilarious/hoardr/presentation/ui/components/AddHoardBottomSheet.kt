package com.sahilarious.hoardr.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sahilarious.hoardr.presentation.ui.theme.Caveat
import com.sahilarious.hoardr.presentation.ui.theme.Quicksand
import com.sahilarious.hoardr.presentation.ui.theme.hoardrColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHoardBottomSheet(
    onDismissRequest: () -> Unit,
    onSaveHoard: (category: String, title: String, url: String?, description: String?) -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
) {
    val colors = MaterialTheme.hoardrColors

    var selectedCategory by remember { mutableStateOf("Article") }
    var title by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val categories = listOf("Article", "Note", "Idea")
    val isSaveEnabled = title.isNotBlank() || url.isNotBlank() || description.isNotBlank()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = colors.surface,
        contentColor = colors.textPrimary,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Add to Hoard",
                    fontFamily = Caveat,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                )

                IconButton(
                    onClick = onDismissRequest,
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = colors.textSecondary,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Category Selection Chips
            Text(
                text = "Category",
                fontFamily = Quicksand,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = colors.textSecondary,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                categories.forEach { category ->
                    val isSelected = category == selectedCategory
                    val chipBg = if (isSelected) colors.primary else colors.surfaceVariant
                    val chipTextColor = if (isSelected) colors.textPrimary else colors.textSecondary

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(chipBg)
                            .clickable { selectedCategory = category }
                            .padding(horizontal = 18.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = category,
                            fontFamily = Quicksand,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                            fontSize = 13.sp,
                            color = chipTextColor,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Title Input
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = {
                    Text(
                        text = "Title",
                        fontFamily = Quicksand,
                    )
                },
                placeholder = {
                    Text(
                        text = if (selectedCategory == "Note") "Note Heading" else "e.g. Jetpack Compose Masterclass",
                        fontFamily = Quicksand,
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.outline,
                    focusedLabelColor = colors.primary,
                    unfocusedLabelColor = colors.textSecondary,
                    focusedTextColor = colors.textPrimary,
                    unfocusedTextColor = colors.textPrimary,
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )

            Spacer(modifier = Modifier.height(12.dp))

            // URL Input (Optional for Notes/Ideas)
            OutlinedTextField(
                value = url,
                onValueChange = { url = it },
                label = {
                    Text(
                        text = "URL (Optional)",
                        fontFamily = Quicksand,
                    )
                },
                placeholder = {
                    Text(
                        text = "https://example.com/article",
                        fontFamily = Quicksand,
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.outline,
                    focusedLabelColor = colors.primary,
                    unfocusedLabelColor = colors.textSecondary,
                    focusedTextColor = colors.textPrimary,
                    unfocusedTextColor = colors.textPrimary,
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Next,
                ),
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Description Input
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = {
                    Text(
                        text = "Description / Notes",
                        fontFamily = Quicksand,
                    )
                },
                placeholder = {
                    Text(
                        text = "Write a quick summary, thoughts or notes...",
                        fontFamily = Quicksand,
                    )
                },
                minLines = 3,
                maxLines = 5,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.outline,
                    focusedLabelColor = colors.primary,
                    unfocusedLabelColor = colors.textSecondary,
                    focusedTextColor = colors.textPrimary,
                    unfocusedTextColor = colors.textPrimary,
                ),
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Save Button
            Button(
                onClick = {
                    if (isSaveEnabled) {
                        onSaveHoard(
                            selectedCategory,
                            title,
                            url.ifBlank { null },
                            description.ifBlank { null },
                        )
                    }
                },
                enabled = isSaveEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.textPrimary,
                    disabledContainerColor = colors.surfaceVariant,
                    disabledContentColor = colors.textTertiary,
                ),
            ) {
                Text(
                    text = "Save Hoard",
                    fontFamily = Quicksand,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                )
            }
        }
    }
}
