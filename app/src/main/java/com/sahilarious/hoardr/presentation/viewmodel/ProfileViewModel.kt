package com.sahilarious.hoardr.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahilarious.hoardr.app.core.Status
import com.sahilarious.hoardr.domain.model.LinkModel
import com.sahilarious.hoardr.domain.repository.LinkRepository
import com.sahilarious.hoardr.presentation.model.ItemCategory
import com.sahilarious.hoardr.presentation.model.toUiItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
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

    suspend fun exportJsonString(): String {
        val dbLinks = linkRepository.getAllLinks().firstOrNull() ?: emptyList()
        val jsonArray = JSONArray()
        dbLinks.forEach { link ->
            val obj = JSONObject().apply {
                put("id", link.id)
                put("url", link.url)
                put("title", link.title)
                put("description", link.description)
                put("imageUrl", link.imageUrl)
                put("category", link.category)
                put("timestamp", link.timestamp)
                put("isRead", link.isRead)
                put("status", link.status.name)
                put("isPriority", link.isPriority)
            }
            jsonArray.put(obj)
        }
        return jsonArray.toString(2)
    }

    fun importFromJson(jsonString: String) {
        viewModelScope.launch {
            try {
                val jsonArray = JSONArray(jsonString)
                val list = mutableListOf<LinkModel>()
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    val link = LinkModel(
                        id = if (obj.has("id")) obj.getLong("id") else 0,
                        url = if (obj.has("url") && !obj.isNull("url")) obj.getString("url") else null,
                        title = if (obj.has("title") && !obj.isNull("title")) obj.getString("title") else null,
                        description = if (obj.has("description") && !obj.isNull("description")) obj.getString("description") else null,
                        imageUrl = if (obj.has("imageUrl") && !obj.isNull("imageUrl")) obj.getString("imageUrl") else null,
                        category = if (obj.has("category")) obj.getString("category") else "Article",
                        timestamp = if (obj.has("timestamp")) obj.getLong("timestamp") else System.currentTimeMillis(),
                        isRead = if (obj.has("isRead")) obj.getBoolean("isRead") else false,
                        status = Status.valueOf(if (obj.has("status")) obj.getString("status") else "PROCESSED"),
                        isPriority = if (obj.has("isPriority")) obj.getBoolean("isPriority") else false,
                    )
                    list.add(link)
                }
                linkRepository.importLinks(list)
            } catch (_: Exception) {}
        }
    }
}
