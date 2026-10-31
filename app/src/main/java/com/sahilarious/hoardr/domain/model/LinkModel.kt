package com.sahilarious.hoardr.domain.model

import com.sahilarious.hoardr.app.core.Status

data class LinkModel(
    val id: Long,
    val url: String?,
    val title: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val category: String = "Article",
    val timestamp: Long,
    val isRead: Boolean,
    val status: Status,
    val isPriority: Boolean = false,
)
