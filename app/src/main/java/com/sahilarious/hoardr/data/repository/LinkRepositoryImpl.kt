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

    private suspend fun saveLink(link: LinkModel) {
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
                Log.d("LinkRepository", "Fetching page content for: $sanitizedUrl")
                val html = fetchHtml(sanitizedUrl)

                if (html != null) {
                    val document = Jsoup.parse(html, sanitizedUrl)
                    val ogTitle = document.select("meta[property=og:title]").attr("content")
                    val title = if (ogTitle.isNotBlank()) {
                        ogTitle
                    } else {
                        val twitterTitle =
                            document.select("meta[name=twitter:title]").attr("content")
                        if (twitterTitle.isNotBlank()) twitterTitle else document.title()
                    }

                    val ogImage = document.select("meta[property=og:image]").attr("abs:content")
                    val imageUrl = if (ogImage.isNotBlank()) {
                        ogImage
                    } else {
                        val rawOgImage = document.select("meta[property=og:image]").attr("content")
                        if (rawOgImage.isNotBlank()) {
                            rawOgImage
                        } else {
                            val twitterImg = document.select("meta[name=twitter:image]").attr("abs:content")
                            if (twitterImg.isNotBlank()) twitterImg else document.select("meta[name=twitter:image]").attr("content")
                        }
                    }.ifBlank { null }

                    if (!title.isNullOrEmpty()) {
                        Log.d("LinkRepository", "Successfully fetched title: $title, imageUrl: $imageUrl")
                        saveLink(link.copy(title = title, imageUrl = imageUrl, status = Status.PROCESSED))
                    } else {
                        Log.w("LinkRepository", "No title found in HTML for $sanitizedUrl")
                        saveLink(link.copy(status = Status.FAILED))
                    }
                } else {
                    Log.e(
                        "LinkRepository",
                        "Failed to fetch HTML for $sanitizedUrl"
                    )
                    saveLink(link.copy(status = Status.FAILED))
                }
            } catch (e: Exception) {
                Log.e("LinkRepository", "Error during enhancement for $sanitizedUrl", e)
                saveLink(link.copy(status = Status.FAILED))
            }
        }
    }

    private suspend fun fetchHtml(targetUrl: String): String? {
        // 1. Try direct fetch first using browser User-Agent (fast & works for most sites)
        val directHtml = fetchHtmlDirect(targetUrl)
        if (!directHtml.isNullOrBlank()) {
            return directHtml
        }

        // 2. Fallback to ZenRows if direct fetch failed
        Log.w("LinkRepository", "Direct fetch failed. Falling back to ZenRows for: $targetUrl")
        return fetchHtmlWithZenRows(targetUrl)
    }

    private suspend fun fetchHtmlDirect(targetUrl: String): String? {
        val request = Request.Builder()
            .url(targetUrl)
            .header(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
            )
            .build()

        return suspendCancellableCoroutine { continuation ->
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.w("LinkRepository", "Direct Fetch Failure: ${e.message}")
                    continuation.resume(null)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        continuation.resume(response.body?.string())
                    } else {
                        Log.w(
                            "LinkRepository",
                            "Direct Fetch HTTP Error: ${response.code} ${response.message}"
                        )
                        continuation.resume(null)
                    }
                    response.close()
                }
            })
        }
    }

    private suspend fun fetchHtmlWithZenRows(targetUrl: String): String? {
        val apiKey = BuildConfig.ZENROWS_API_KEY
        if (apiKey.isBlank()) {
            Log.w("LinkRepository", "ZenRows API key is empty")
            return null
        }

        val encodedUrl = URLEncoder.encode(targetUrl, "UTF-8")
        val zenRowsUrl = "https://api.zenrows.com/v1/?apikey=$apiKey&url=$encodedUrl"

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
