package com.sahilarious.hoardr.domain.repository

import com.sahilarious.hoardr.domain.model.LinkModel
import kotlinx.coroutines.flow.Flow

interface LinkRepository {

    fun getAllLinks(): Flow<List<LinkModel>>
    fun getUnreadLinks(): Flow<List<LinkModel>>
    fun getPriorityLinks(): Flow<List<LinkModel>>

    suspend fun getLinkById(id: Long): LinkModel?
    suspend fun addLink(link: LinkModel)
    suspend fun deleteLink(id: Long)
    suspend fun clearReadLinks()
    suspend fun updateReadStatus(id: Long, isRead: Boolean)
    suspend fun updatePriority(id: Long, isPriority: Boolean)
    suspend fun performEnhancement(link: LinkModel)
}
