package com.sahilarious.hoardr.domain.repository

import com.sahilarious.hoardr.domain.model.LinkModel
import kotlinx.coroutines.flow.Flow

interface LinkRepository {

    fun getAllLinks(): Flow<List<LinkModel>>

    suspend fun getLinkById(id: Long): LinkModel?
    suspend fun deleteLink(link: LinkModel)
    suspend fun performEnhancement(link: LinkModel)
}