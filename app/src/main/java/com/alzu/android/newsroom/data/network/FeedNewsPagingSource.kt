package com.alzu.android.newsroom.data.network

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alzu.android.newsroom.data.mapper.ArticleMapper
import com.alzu.android.newsroom.data.network.model.ArticleDto
import com.alzu.android.newsroom.data.network.model.NewsResponseDto
import com.alzu.android.newsroom.domain.ArticleEntity
import kotlinx.coroutines.*
import net.mm2d.touchicon.IconComparator
import net.mm2d.touchicon.TouchIconExtractor
import retrofit2.HttpException
import java.lang.Exception
import java.net.MalformedURLException

class FeedNewsPagingSource(
    private val backend: NewsAPI = RetrofitInstance.api,
    private val query: String
) : PagingSource<Int, ArticleEntity>() {
    companion object{
        const val TAG = "NewsPagingSource"
        const val MAX_PAGE_SIZE = 20
    }

    private val mapper = ArticleMapper()

    override fun getRefreshKey(state: PagingState<Int, ArticleEntity>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleEntity> {
        try {
            if (query.isEmpty()) {
                return LoadResult.Page(emptyList(), prevKey = null, nextKey = null)
            }
            val pageIndex: Int = params.key ?: 1
            val pageSize: Int = params.loadSize.coerceAtMost(MAX_PAGE_SIZE)

            val response = backend.getBreakingNews(query, pageIndex, 1)

            if (response.isSuccessful) {
                response.body()?.let { resultResponse ->
                    coroutineScope { launch(Dispatchers.IO) { addIcons(resultResponse.articles) } }
                }
                val newsResponseDto: NewsResponseDto = checkNotNull(response.body())
                Log.i(TAG, "total results: ${newsResponseDto.totalResults}")
                val nextKey = if (newsResponseDto.totalResults < pageSize) null else pageIndex + 1
                val prevKey = if (pageIndex == 1) null else pageIndex - 1
                return LoadResult.Page(newsResponseDto.articles.map { mapper.mapArticleDtoToEntity(it) }, prevKey, nextKey)
            } else {
                Log.i(TAG, "server is not responding (probably your API-KEY has expired)")
                return LoadResult.Error(HttpException(response))
            }
        } catch (ex: Exception) {
            Log.i(TAG, "No internet connection!")
            return LoadResult.Error(ex)
        }
    }

    private suspend fun addIcons(list: List<ArticleDto>) {
        coroutineScope {
            launch(Dispatchers.IO) {
                val extractor = TouchIconExtractor()
                for (art in list) {
                    try {
                        val tmp = getIcon(extractor, art.url)
                        art.iconUrl = tmp.await()
                    } catch (e: MalformedURLException) {
                        Log.i(TAG, "fail to get icon")
                    }
                }
            }
        }
    }

    private suspend fun getIcon(
        extractor: TouchIconExtractor,
        address: String
    ): Deferred<String> {
        var result: Deferred<String>
        coroutineScope {
            val job = async(Dispatchers.IO) {
                var path = ""
                try {
                    val job = async(Dispatchers.IO) {
                        extractor.fromPage(address, true)
                    }
                    val icons = job.await()
                    Log.i(TAG, "total icons: ${icons.size}")
                    val bestIcon1 = icons.maxWithOrNull(IconComparator.REL_SIZE)
                    bestIcon1?.url?.let {
                        path = it
                    }
                } catch (e: MalformedURLException) {
                    Log.i(TAG, "cannot parse from: $address")
                }
                path
            }
            result = job
        }
        return result
    }
}