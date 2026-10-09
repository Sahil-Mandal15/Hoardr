package com.sahilarious.hoardr.presentation.model

import com.sahilarious.hoardr.domain.model.LinkModel
import java.net.URI

enum class ItemCategory {
    ARTICLE, NOTE, IDEA
}

data class HoardrUiItem(
    val id: Long,
    val title: String,
    val description: String? = null,
    val category: ItemCategory,
    val sourceOrFolder: String,
    val timeAgo: String,
    val imageUrl: String?,
    val rawUrl: String? = null,
)

fun LinkModel.toUiItem(): HoardrUiItem {
    val cleanHost = extractHost(url)
    val displayTitle = if (!title.isNullOrBlank()) {
        title
    } else {
        cleanHost
    }

    val itemCategory = when (category.lowercase()) {
        "note" -> ItemCategory.NOTE
        "idea" -> ItemCategory.IDEA
        else -> ItemCategory.ARTICLE
    }

    return HoardrUiItem(
        id = id,
        title = displayTitle,
        description = description,
        category = itemCategory,
        sourceOrFolder = cleanHost,
        timeAgo = formatTimeAgo(timestamp),
        imageUrl = imageUrl,
        rawUrl = url,
    )
}

fun extractHost(rawUrl: String?): String {
    if (rawUrl.isNullOrBlank()) return "Saved Link"
    val formattedUrl = if (!rawUrl.startsWith("http://") && !rawUrl.startsWith("https://")) {
        "https://$rawUrl"
    } else {
        rawUrl
    }

    return try {
        val uri = URI(formattedUrl)
        val host = uri.host
        if (!host.isNullOrBlank()) {
            if (host.startsWith("www.")) host.substring(4) else host
        } else {
            "Saved Link"
        }
    } catch (_: Exception) {
        "Saved Link"
    }
}

fun formatTimeAgo(timestamp: Long): String {
    if (timestamp <= 0) return "Recently"
    val diff = System.currentTimeMillis() - timestamp
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val weeks = days / 7

    return when {
        weeks > 0 -> "$weeks week${if (weeks > 1) "s" else ""} ago"
        days > 0 -> "$days day${if (days > 1) "s" else ""} ago"
        hours > 0 -> "$hours hr${if (hours > 1) "s" else ""} ago"
        minutes > 0 -> "$minutes min${if (minutes > 1) "s" else ""} ago"
        else -> "Just now"
    }
}
