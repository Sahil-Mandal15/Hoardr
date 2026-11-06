package com.sahilarious.hoardr.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahilarious.hoardr.domain.repository.LinkRepository
import com.sahilarious.hoardr.presentation.model.ItemCategory
import com.sahilarious.hoardr.presentation.model.toUiItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val totalHoards: Int = 0,
    val unreadCount: Int = 0,
    val readCount: Int = 0,
    val priorityCount: Int = 0,
    val articleCount: Int = 0,
    val noteCount: Int = 0,
    val ideaCount: Int = 0,
    val readPercentage: Int = 0,
    val isLoading: Boolean = false,
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val linkRepository: LinkRepository,
) : ViewModel() {

    val uiState: StateFlow<ProfileUiState> = linkRepository.getAllLinks()
        .map { dbLinks ->
            val uiItems = dbLinks.map { it.toUiItem() }
            val total = uiItems.size
            val unread = uiItems.count { !it.isRead }
            val read = uiItems.count { it.isRead }
            val priority = uiItems.count { it.isPriority }
            val articles = uiItems.count { it.category == ItemCategory.ARTICLE }
            val notes = uiItems.count { it.category == ItemCategory.NOTE }
            val ideas = uiItems.count { it.category == ItemCategory.IDEA }
            val percentage = if (total > 0) (read * 100) / total else 0

            ProfileUiState(
                totalHoards = total,
                unreadCount = unread,
                readCount = read,
                priorityCount = priority,
                articleCount = articles,
                noteCount = notes,
                ideaCount = ideas,
                readPercentage = percentage,
                isLoading = false,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileUiState(isLoading = true),
        )

    fun clearReadItems() {
        viewModelScope.launch {
            linkRepository.clearReadLinks()
        }
    }
}
