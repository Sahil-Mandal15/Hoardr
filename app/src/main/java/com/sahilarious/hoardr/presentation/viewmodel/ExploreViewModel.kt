package com.sahilarious.hoardr.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahilarious.hoardr.domain.repository.LinkRepository
import com.sahilarious.hoardr.presentation.model.HoardrUiItem
import com.sahilarious.hoardr.presentation.model.ItemCategory
import com.sahilarious.hoardr.presentation.model.toUiItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExploreUiState(
    val articles: List<HoardrUiItem> = emptyList(),
    val isLoading: Boolean = false,
)

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val linkRepository: LinkRepository,
) : ViewModel() {

    private val _dismissedIds = MutableStateFlow<Set<Long>>(emptySet())
    private val _priorityOverrides = MutableStateFlow<Map<Long, Boolean>>(emptyMap())

    val uiState: StateFlow<ExploreUiState> = combine(
        linkRepository.getUnreadLinks(),
        _dismissedIds,
        _priorityOverrides,
    ) { dbLinks, dismissedIds, priorityOverrides ->
        val articles = dbLinks
            .map { link ->
                val uiItem = link.toUiItem()
                val isPriority = priorityOverrides[uiItem.id] ?: uiItem.isPriority
                uiItem.copy(isPriority = isPriority)
            }
            .filter { it.category == ItemCategory.ARTICLE && !dismissedIds.contains(it.id) }
        ExploreUiState(
            articles = articles,
            isLoading = false,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ExploreUiState(isLoading = true),
    )

    fun deleteArticle(item: HoardrUiItem) {
        if (item.id < 0) return
        viewModelScope.launch {
            linkRepository.deleteLink(item.id)
        }
    }

    fun dismissArticle(item: HoardrUiItem) {
        if (item.id < 0) return
        _dismissedIds.value = _dismissedIds.value + item.id
    }

    fun markAsRead(item: HoardrUiItem) {
        if (item.id < 0) return
        viewModelScope.launch {
            linkRepository.updateReadStatus(item.id, isRead = true)
        }
    }

    fun togglePriority(item: HoardrUiItem) {
        if (item.id < 0) return
        val newPriority = !item.isPriority
        _priorityOverrides.value = _priorityOverrides.value + (item.id to newPriority)
        viewModelScope.launch {
            linkRepository.updatePriority(item.id, newPriority)
        }
    }
}
