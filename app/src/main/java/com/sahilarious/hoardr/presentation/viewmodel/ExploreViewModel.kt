package com.sahilarious.hoardr.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahilarious.hoardr.domain.repository.LinkRepository
import com.sahilarious.hoardr.presentation.model.HoardrUiItem
import com.sahilarious.hoardr.presentation.model.ItemCategory
import com.sahilarious.hoardr.presentation.model.toUiItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
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

    val uiState: StateFlow<ExploreUiState> = linkRepository.getUnreadLinks()
        .map { dbLinks ->
            val articles = dbLinks
                .map { it.toUiItem() }
                .filter { it.category == ItemCategory.ARTICLE }
            ExploreUiState(
                articles = articles,
                isLoading = false,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ExploreUiState(isLoading = true),
        )

    fun deleteArticle(item: HoardrUiItem) {
        if (item.id < 0) return
        viewModelScope.launch {
            val linkModel = linkRepository.getLinkById(item.id) ?: return@launch
            linkRepository.deleteLink(linkModel)
        }
    }
}
