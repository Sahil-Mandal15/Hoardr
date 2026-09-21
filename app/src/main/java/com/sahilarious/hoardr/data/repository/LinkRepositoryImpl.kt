package com.sahilarious.hoardr.data.repository

import android.content.Context
import android.util.Log
import com.sahilarious.hoardr.BuildConfig
import com.sahilarious.hoardr.app.core.Status
import com.sahilarious.hoardr.data.db.HoardrDao
import com.sahilarious.hoardr.data.mappers.toDomain
import com.sahilarious.hoardr.data.mappers.toEntity
import com.sahilarious.hoardr.di.ScraperClient
import com.sahilarious.hoardr.domain.model.LinkModel
import com.sahilarious.hoardr.domain.repository.LinkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jsoup.Jsoup
import java.io.IOException
import java.net.URLEncoder
import javax.inject.Inject
import kotlin.coroutines.resume

class LinkRepositoryImpl @Inject constructor(
    private val hoardrDao: HoardrDao,
    private val context: Context,
    @ScraperClient private val client: OkHttpClient,
) : LinkRepository {

    override fun getAllLinks(): Flow<List<LinkModel>> = hoardrDao.getAllLinks().map { entityList ->
        entityList.map { it.toDomain() }
    }

    override suspend fun getLinkById(id: Long): LinkModel? {
        return hoardrDao.getAllLinks()
            .map { entities -> entities.map { it.toDomain() } }
            .firstOrNull() // Takes the first snapshot list from the DB Flow stream
            ?.firstOrNull { it.id == id }
    }

    override suspend fun saveLink(link: LinkModel) {
        hoardrDao.insertLink(link.toEntity())
    }

    override suspend fun deleteLink(link: LinkModel) {
        hoardrDao.deleteLinkById(link.toEntity())
    }

    override suspend fun performEnhancement(link: LinkModel) {
        val rawUrl = link.url ?: return
        val sanitizedUrl = rawUrl.trim().split(Regex("\\s+")).firstOrNull() ?: return

        withContext(Dispatchers.IO + NonCancellable) {
            try {
                Log.d("LinkRepository", "Fetching title for: $sanitizedUrl")
                val html = fetchHtmlWithZenRows(sanitizedUrl)

                if (html != null) {
                    val document = Jsoup.parse(html)
                    val ogTitle = document.select("meta[property=og:title]").attr("content")
                    val title = if (ogTitle.isNotBlank()) {
                        ogTitle
                    } else {
                        val twitterTitle =
                            document.select("meta[name=twitter:title]").attr("content")
                        if (twitterTitle.isNotBlank()) twitterTitle else document.title()
                    }

                    if (!title.isNullOrEmpty()) {
                        Log.d("LinkRepository", "Successfully fetched title: $title")
                        saveLink(link.copy(title = title, status = Status.PROCESSED))
                    } else {
                        Log.w("LinkRepository", "No title found in HTML for $sanitizedUrl")
                        saveLink(link.copy(status = Status.FAILED))
                    }
                } else {
                    Log.e(
                        "LinkRepository",
                        "Failed to fetch HTML from ZenRows (likely timeout or API error)"
                    )
                    saveLink(link.copy(status = Status.FAILED))
                }
            } catch (e: Exception) {
                Log.e("LinkRepository", "Error during enhancement for $sanitizedUrl", e)
                saveLink(link.copy(status = Status.FAILED))
            }
        }
    }

    private suspend fun fetchHtmlWithZenRows(targetUrl: String): String? {
        val apiKey = BuildConfig.ZENROWS_API_KEY
        val encodedUrl = URLEncoder.encode(targetUrl, "UTF-8")
        val zenRowsUrl =
            "https://api.zenrows.com/v1/?apikey=$apiKey&url=$encodedUrl&js_render=true&premium_proxy=true"

        val request = Request.Builder()
            .url(zenRowsUrl)
            .build()

        return suspendCancellableCoroutine { continuation ->
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("LinkRepository", "ZenRows Request Failure: ${e.message}")
                    continuation.resume(null)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        continuation.resume(response.body?.string())
                    } else {
                        Log.e(
                            "LinkRepository",
                            "ZenRows API Error: ${response.code} ${response.message}"
                        )
                        continuation.resume(null)
                    }
                    response.close()
                }
            })
        }
    }
}
