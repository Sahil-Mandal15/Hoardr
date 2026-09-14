package com.sahilarious.hoardr.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sahilarious.hoardr.app.core.Status

@Entity
data class LinkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val url: String?,
    val title: String?,
    val timestamp: Long,
    val isRead: Boolean,
    val status: Status
)
