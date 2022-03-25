package com.alzu.android.newsroom.repository


import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alzu.android.newsroom.api.NewsAPI
import com.alzu.android.newsroom.api.RetrofitInstance
import com.alzu.android.newsroom.data.Article
import com.alzu.android.newsroom.data.NewsResponse
import com.alzu.android.newsroom.utils.Constants
import kotlinx.coroutines.*
import net.mm2d.touchicon.IconComparator
import net.mm2d.touchicon.TouchIconExtractor
import retrofit2.HttpException
import java.lang.Exception
import java.net.MalformedURLException

class SearchNewsPagingSource(
    private val backend: NewsAPI = RetrofitInstance.api,
    private val query: String
) : PagingSource<Int, Article>() {
    val TAG = "SearchNewsPagingSource"

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        try {
            if (query.isEmpty()) {
                return LoadResult.Page(emptyList(), prevKey = null, nextKey = null)
            }
            val pageIndex: Int = params.key ?: 1
            val pageSize: Int = params.loadSize.coerceAtMost(Constants.MAX_PAGE_SIZE)

            val response = backend.searchForNews(query, pageIndex, 1)
            if (response.isSuccessful) {
                response.body()?.let { resultResponse ->
                    coroutineScope { launch(Dispatchers.IO) { addIcons(resultResponse.articles) } }
                }
                val newsResponse: NewsResponse = checkNotNull(response.body())
                Log.i(TAG, "total results: ${newsResponse.totalResults}")
                val nextKey = if (newsResponse.totalResults < pageSize) null else pageIndex + 1
                val prevKey = if (pageIndex == 1) null else pageIndex - 1
                return LoadResult.Page(newsResponse.articles, prevKey, nextKey)
            } else {
                Log.i(TAG, "server is not responding (probably your API-KEY has expired)")
                return LoadResult.Error(HttpException(response))
            }
        } catch (ex: Exception) {
            Log.i(TAG, "No internet connection!")
            return LoadResult.Error(ex)
        }
    }

    private suspend fun addIcons(list: List<Article>) {
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