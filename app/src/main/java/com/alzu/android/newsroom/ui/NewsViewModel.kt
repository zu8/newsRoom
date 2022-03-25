package com.alzu.android.newsroom.ui


import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.preference.PreferenceManager
import com.alzu.android.newsroom.api.Country
import com.alzu.android.newsroom.api.RetrofitInstance
import com.alzu.android.newsroom.data.Article
import com.alzu.android.newsroom.repository.FeedNewsPagingSource
import com.alzu.android.newsroom.repository.NewsRepository
import com.alzu.android.newsroom.repository.SearchNewsPagingSource
import com.alzu.android.newsroom.utils.Constants.Companion.DELAY_TO_SEARCH
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class NewsViewModel(
    app: Application,
    val newsRepository: NewsRepository
) : AndroidViewModel(app) {
    private val TAG = "NewsViewModel"

    // I use AndroidViewModel instead ViewModel because i need to access to sh.pref to set
    // country when NewsViewModel started. To use sheared preferences I need context
    private val sp = PreferenceManager.getDefaultSharedPreferences(app.applicationContext)
    private val chosenCountry = sp.getString("currentCountry", "us") ?: "noCountry"

    private val searchQuery = MutableLiveData("")
    private val region = MutableLiveData(chosenCountry)
    val searchNewsFlow: Flow<PagingData<Article>>
    val breakingNewsFlow: Flow<PagingData<Article>>

    init {
        breakingNewsFlow = region.asFlow()
            .flatMapLatest { breakingNewsMod(it) }.cachedIn(viewModelScope)

        searchNewsFlow = searchQuery.asFlow()
            .debounce(DELAY_TO_SEARCH)
            .flatMapLatest { searchNewsMod(it) }.cachedIn(viewModelScope)
    }

    private fun breakingNewsMod(query: String): Flow<PagingData<Article>> {
        Log.i(TAG, "country for feed: $chosenCountry")
        return Pager(
            PagingConfig(pageSize = 5),
            pagingSourceFactory = { FeedNewsPagingSource(RetrofitInstance.api, query) })
            .flow.cachedIn(viewModelScope)
    }

    fun setSearchQuery(query: String) {
        if (searchQuery.value == query) return
        searchQuery.value = query
    }

    fun setRegion(reg: String) {
        if (reg == "") return
        var condition = false
        for (ter in Country.values()) {
            if (ter.country == reg) condition = true
        }
        if (condition) region.value = reg
    }

    fun refreshFlow() {
        val tmpLang = region.value
        region.value = Country.EMPTY.country
        region.value = tmpLang
    }

    private fun searchNewsMod(query: String): Flow<PagingData<Article>> {
        return Pager(
            PagingConfig(pageSize = 5),
            pagingSourceFactory = { SearchNewsPagingSource(RetrofitInstance.api, query) })
            .flow.cachedIn(viewModelScope)
    }

    fun saveArticle(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.insertArticle(article)
        }
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.deleteArticle(article)
        }
    }
}