package com.sahilarious.hoardr.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahilarious.hoardr.app.core.Status
import com.sahilarious.hoardr.domain.model.LinkModel
import com.sahilarious.hoardr.domain.repository.LinkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    private val linkRepository: LinkRepository,
) : ViewModel() {

    fun insertLink(url: String) {
        viewModelScope.launch {
            val link = LinkModel(
                id = 0,
                url = url,
                category = "Article",
                timestamp = System.currentTimeMillis(),
                isRead = false,
                status = Status.UNPROCESSED,
            )
            linkRepository.performEnhancement(link)
        }
    }
}
