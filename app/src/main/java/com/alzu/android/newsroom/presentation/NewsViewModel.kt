package com.alzu.android.newsroom.presentation

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.preference.PreferenceManager
import com.alzu.android.newsroom.domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class NewsViewModel @Inject constructor(
    private val newsFeed: GetNewsFeedUseCase,
    private val searchFeed: SearchNewsUseCase,
    private val getSavedNewsListUseCase: GetSavedNewsListUseCase,
    private val insertArticleUseCase: InsertArticleUseCase,
    private val deleteArticleUseCase: DeleteArticleUseCase,
    private val app: Application
) : ViewModel() {

    companion object{
        const val EMPTY_COUNTRY = ""
        const val DELAY_TO_SEARCH = 500L
        const val EMPTY_QUERY = ""
    }

    private val sp = PreferenceManager.getDefaultSharedPreferences(app.applicationContext)
    private val chosenCountry = sp.getString("currentCountry", "us") ?: "noCountry"

    lateinit var news: Flow<PagingData<ArticleEntity>>
    lateinit var searchResult: Flow<PagingData<ArticleEntity>>

    private val region = MutableLiveData(chosenCountry)
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
        /*var condition = false
        for (ter in NewsAPI.Companion.Country.values()) {
            if (ter.country == reg) condition = true
        }
        if (condition) region.value = reg*/
        region.value = reg
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