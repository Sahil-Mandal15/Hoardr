package com.sahilarious.hoardr.data.repository

import com.sahilarious.hoardr.data.db.HoardrDao
import com.sahilarious.hoardr.data.db.HoardrDatabase
import com.sahilarious.hoardr.data.mappers.toDomain
import com.sahilarious.hoardr.data.mappers.toEntity
import com.sahilarious.hoardr.domain.model.LinkModel
import com.sahilarious.hoardr.domain.repository.LinkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LinkRepositoryImpl @Inject constructor(
    private val hoardrDao: HoardrDao
) : LinkRepository {
    override fun getAllLinks(): Flow<List<LinkModel>> = flow {
        val links = hoardrDao.getAllLinks().map { it.toDomain() }
        emit(links)
    }

    override suspend fun getLinkById(id: Long): LinkModel? {
        val link = hoardrDao.getAllLinks().map { it.toDomain() }.firstOrNull { it.id == id }
        return link
    }

    override suspend fun saveLink(link: LinkModel) {
        hoardrDao.insertLink(link.toEntity())
    }

    override suspend fun deleteLink(link: LinkModel) {
        hoardrDao.deleteLinkById(link.toEntity())
    }
}