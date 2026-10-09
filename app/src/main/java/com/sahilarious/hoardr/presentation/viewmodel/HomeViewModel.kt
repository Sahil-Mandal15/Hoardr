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

data class HomeUiState(
    val items: List<HoardrUiItem> = emptyList(),
    val selectedCategory: String = "All",
    val isLoading: Boolean = false,
)

val sampleShowcaseItems = listOf(
    HoardrUiItem(
        id = -1,
        title = "The Psychology of Better Habits",
        category = ItemCategory.ARTICLE,
        sourceOrFolder = "medium.com",
        timeAgo = "2 days ago",
        imageUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb",
    ),
    HoardrUiItem(
        id = -2,
        title = "Android ViewModel – Quick Notes",
        category = ItemCategory.NOTE,
        sourceOrFolder = "My Notes",
        timeAgo = "3 days ago",
        imageUrl = "https://images.unsplash.com/photo-1555066931-4365d14bab8c",
    ),
    HoardrUiItem(
        id = -3,
        title = "Build a minimal habit tracker app",
        category = ItemCategory.IDEA,
        sourceOrFolder = "Personal Ideas",
        timeAgo = "5 days ago",
        imageUrl = "https://images.unsplash.com/photo-1517842645767-c639042777db",
    ),
    HoardrUiItem(
        id = -4,
        title = "Shutter Island – Why it still hits different",
        category = ItemCategory.ARTICLE,
        sourceOrFolder = "theatlantic.com",
        timeAgo = "6 days ago",
        imageUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23",
    ),
    HoardrUiItem(
        id = -5,
        title = "Kolkata places to visit this winter",
        category = ItemCategory.NOTE,
        sourceOrFolder = "Travel Plans",
        timeAgo = "1 week ago",
        imageUrl = "https://images.unsplash.com/photo-1513694203232-719a280e022f",
    ),
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val linkRepository: LinkRepository,
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow("All")

    val uiState: StateFlow<HomeUiState> = combine(
        linkRepository.getAllLinks(),
        _selectedCategory,
    ) { dbLinks, category ->
        val uiItems = dbLinks.map { it.toUiItem() }

        HomeUiState(
            items = filterItems(uiItems, category),
            selectedCategory = category,
            isLoading = false,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(isLoading = true, items = emptyList()),
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

    private fun filterItems(items: List<HoardrUiItem>, category: String): List<HoardrUiItem> {
        return when (category) {
            "Articles" -> items.filter { it.category == ItemCategory.ARTICLE }
            "Notes" -> items.filter { it.category == ItemCategory.NOTE }
            "Ideas" -> items.filter { it.category == ItemCategory.IDEA }
            else -> items
        }
    }
}
