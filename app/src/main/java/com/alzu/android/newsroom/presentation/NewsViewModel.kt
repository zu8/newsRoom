package com.alzu.android.newsroom.presentation

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.preference.PreferenceManager
import com.alzu.android.newsroom.data.network.NewsAPI
import com.alzu.android.newsroom.data.repository.NewsRepositoryImpl
import com.alzu.android.newsroom.domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class NewsViewModel(
    app: Application
) : AndroidViewModel(app) {

    companion object{
        const val EMPTY_COUNTRY = ""
        const val DELAY_TO_SEARCH = 500L
        const val EMPTY_QUERY = ""
    }


    private val newsRepository = NewsRepositoryImpl(app)

    private val sp = PreferenceManager.getDefaultSharedPreferences(app.applicationContext)
    private val chosenCountry = sp.getString("currentCountry", "us") ?: "noCountry"

    lateinit var news: Flow<PagingData<ArticleEntity>>
    lateinit var searchResult: Flow<PagingData<ArticleEntity>>


    private val newsFeed = GetNewsFeedUseCase(newsRepository)
    private val region = MutableLiveData(chosenCountry)

    private val searchFeed = SearchNewsUseCase(newsRepository)
    private val searchQuery = MutableLiveData(EMPTY_QUERY)

    init{
        viewModelScope.launch {
             news =  region.asFlow()
                 .flatMapLatest { newsFeed(it) }
                 .cachedIn(viewModelScope)
        }
        viewModelScope.launch {
            searchResult = searchQuery.asFlow()
                .debounce(DELAY_TO_SEARCH)
                .flatMapLatest { searchFeed(it) }
                .cachedIn(viewModelScope)
        }
    }



    private val getSavedNewsListUseCase = GetSavedNewsListUseCase(newsRepository)
    private val insertArticleUseCase = InsertArticleUseCase(newsRepository)
    private val deleteArticleUseCase = DeleteArticleUseCase(newsRepository)

    private var _savedArticlesList: MutableLiveData<List<ArticleEntity>> =
        getSavedNewsListUseCase() as MutableLiveData<List<ArticleEntity>>

    val savedArticles = _savedArticlesList


    fun saveArticle(article: ArticleEntity) {
        viewModelScope.launch {
            insertArticleUseCase(article)
        }
    }

    fun deleteArticle(article: ArticleEntity){
        viewModelScope.launch {
            deleteArticleUseCase(article)
        }
    }

    fun setRegion(reg: String){
        if (reg == EMPTY_COUNTRY) return
        var condition = false
        for (ter in NewsAPI.Companion.Country.values()) {
            if (ter.country == reg) condition = true
        }
        if (condition) region.value = reg
    }

    fun setSearchQuery(query: String) {
        if (searchQuery.value == query) return
        searchQuery.value = query
    }

    fun refreshFlow() {
        val tmpLang = region.value
        region.value = EMPTY_COUNTRY
        region.value = tmpLang
    }

}