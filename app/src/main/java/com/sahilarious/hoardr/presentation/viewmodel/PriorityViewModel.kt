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

data class PriorityUiState(
    val items: List<HoardrUiItem> = emptyList(),
    val selectedCategory: String = "All",
    val isLoading: Boolean = false,
)

@HiltViewModel
class PriorityViewModel @Inject constructor(
    private val linkRepository: LinkRepository,
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow("All")

    val uiState: StateFlow<PriorityUiState> = combine(
        linkRepository.getPriorityLinks(),
        _selectedCategory,
    ) { dbLinks, category ->
        val uiItems = dbLinks.map { it.toUiItem() }
        PriorityUiState(
            items = filterItems(uiItems, category),
            selectedCategory = category,
            isLoading = false,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PriorityUiState(isLoading = true, items = emptyList()),
    )

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
    }

    fun deleteItem(item: HoardrUiItem) {
        if (item.id < 0) return
        viewModelScope.launch {
            val linkModel = linkRepository.getLinkById(item.id) ?: return@launch
            linkRepository.deleteLink(linkModel)
        }
    }

    fun markAsRead(item: HoardrUiItem) {
        if (item.id < 0) return
        viewModelScope.launch {
            linkRepository.updateReadStatus(item.id, isRead = true)
        }
    }

    fun togglePriority(item: HoardrUiItem) {
        if (item.id < 0) return
        viewModelScope.launch {
            val linkModel = linkRepository.getLinkById(item.id) ?: return@launch
            linkRepository.updatePriority(item.id, !linkModel.isPriority)
        }
    }

    private fun filterItems(items: List<HoardrUiItem>, category: String): List<HoardrUiItem> {
        return when (category) {
            "Articles" -> items.filter { it.category == ItemCategory.ARTICLE }
            "Notes" -> items.filter { it.category == ItemCategory.NOTE }
            "Ideas" -> items.filter { it.category == ItemCategory.IDEA }
            else -> items
        }
    }
}
