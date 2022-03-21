package com.alzu.android.newsroom.ui

import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alzu.android.newsroom.data.Article
import com.alzu.android.newsroom.data.NewsResponse
import com.alzu.android.newsroom.repository.NewsRepository
import com.alzu.android.newsroom.utils.Resource
import kotlinx.coroutines.*
import retrofit2.Response
import net.mm2d.touchicon.IconComparator
import net.mm2d.touchicon.TouchIconExtractor
import java.net.MalformedURLException

class NewsViewModel(
    val newsRepository: NewsRepository
) : ViewModel() {
    private val TAG = "NewsViewModel"
    private val _breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val breakingNews: LiveData<Resource<NewsResponse>> = _breakingNews
    var breakingNewsPage = 1
    private val _searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchNews: LiveData<Resource<NewsResponse>> = _searchNews
    var searchNewsPage = 1

    init {
        getBreakingNews("us")
    }

    private fun getBreakingNews(countryCode: String) {
         viewModelScope.launch(Dispatchers.IO){
            _breakingNews.postValue(Resource.Loading())
            val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
            _breakingNews.postValue(handleBreakingNewsResponse(response))
        }
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        _searchNews.postValue(Resource.Loading())
        val response = newsRepository.searchNews(searchQuery, searchNewsPage)
        _searchNews.postValue(handleSearchNewsResponse(response))
    }

    private suspend fun handleBreakingNewsResponse(
        response: Response<NewsResponse>
    ): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                val job = viewModelScope.launch(Dispatchers.IO) { addIcons(resultResponse.articles) } // resultResponse.articles
                job.join()
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun handleSearchNewsResponse(
        response: Response<NewsResponse>
    ): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                val job = viewModelScope.launch(Dispatchers.IO) { addIcons(resultResponse.articles) }  // resultResponse.articles
                job.join()
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) {
        viewModelScope.launch(Dispatchers.IO){
            newsRepository.insertArticle(article)
        }
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article){
        viewModelScope.launch(Dispatchers.IO){
            newsRepository.deleteArticle(article)
        }
    }

        private suspend fun addIcons(list: List<Article>) = viewModelScope.launch(Dispatchers.IO) {
        val extractor = TouchIconExtractor()
        for (art in list) {
            try {
                val tmp = getIcon(extractor, art.url)
                art.iconUrl = tmp.await()
            }
            catch (e: MalformedURLException){
                Log.i(TAG,"fail to get icon")
            }
        }
    }

    suspend fun getIcon(
        extractor: TouchIconExtractor,
        address: String
    ): Deferred<String> = viewModelScope.async(Dispatchers.IO) {
        var path = ""
            try {
            val job = async(Dispatchers.IO) {
                extractor.fromPage(address, true)
            }
            val icons = job.await()
            Log.i(TAG, "${icons.size}")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val bestIcon1 = icons.maxWithOrNull(IconComparator.REL_SIZE)
                bestIcon1?.url?.let {
                    path = it
                }
            }
        }
        catch (e: MalformedURLException){
            Log.i(TAG,"cannot parse from: $address")
        }
        path
    }
}

fun getSourceAddress(articleUrl: String): String {
    val index = articleUrl.indexOf('/')
    val modStr = articleUrl.substring(index + 2)
    val address = modStr.substringBefore('/')
    return "${articleUrl.substring(0, index + 2)}${address}"
}



