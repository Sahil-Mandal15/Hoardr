package com.sahilarious.hoardr.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahilarious.hoardr.app.core.Status
import com.sahilarious.hoardr.domain.model.LinkModel
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

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val linkRepository: LinkRepository,
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow("All")
    private val _priorityOverrides = MutableStateFlow<Map<Long, Boolean>>(emptyMap())

    val uiState: StateFlow<HomeUiState> = combine(
        linkRepository.getUnreadLinks(),
        _selectedCategory,
        _priorityOverrides,
    ) { dbLinks, category, priorityOverrides ->
        val uiItems = dbLinks.map { link ->
            val uiItem = link.toUiItem()
            val isPriority = priorityOverrides[uiItem.id] ?: uiItem.isPriority
            uiItem.copy(isPriority = isPriority)
        }

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

    fun addHoard(
        category: String,
        title: String,
        url: String?,
        description: String?,
    ) {
        viewModelScope.launch {
            val extractedFromUrl = extractUrl(url)
            val extractedFromTitle = if (extractedFromUrl == null) extractUrl(title) else null

            val finalUrl = extractedFromUrl ?: extractedFromTitle

            val finalTitle = when {
                extractedFromTitle != null -> null
                !finalUrl.isNullOrBlank() && title.trim()
                    .equals(finalUrl, ignoreCase = true) -> null

                title.isNotBlank() -> title.trim()
                else -> null
            }

            val linkModel = LinkModel(
                id = 0,
                url = finalUrl,
                title = finalTitle,
                description = description?.ifBlank { null }?.trim(),
                category = category,
                timestamp = System.currentTimeMillis(),
                isRead = false,
                status = if (!finalUrl.isNullOrBlank()) Status.UNPROCESSED else Status.PROCESSED,
            )
            linkRepository.addLink(linkModel)
        }
    }

    private fun extractUrl(text: String?): String? {
        if (text.isNullOrBlank()) return null
        val trimmed = text.trim()
        val urlRegex =
            Regex("""(?i)\b(?:https?://|www\.)[^\s<>"'()]+\b|\b[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}(?:/[^\s<>"'()]*)?\b""")
        val match = urlRegex.find(trimmed)?.value
        if (match != null) {
            return if (!match.startsWith("http://") && !match.startsWith("https://")) {
                "https://$match"
            } else {
                match
            }
        }
        return null
    }

    fun deleteItem(item: HoardrUiItem) {
        if (item.id < 0) return
        viewModelScope.launch {
            linkRepository.deleteLink(item.id)
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
        val newPriority = !item.isPriority
        _priorityOverrides.value = _priorityOverrides.value + (item.id to newPriority)
        viewModelScope.launch {
            linkRepository.updatePriority(item.id, newPriority)
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
