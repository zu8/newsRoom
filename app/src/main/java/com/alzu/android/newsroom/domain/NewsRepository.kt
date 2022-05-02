package com.alzu.android.newsroom.domain

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow


interface NewsRepository {

    suspend fun insertArticle(article: ArticleEntity)

    fun getSavedNews(): LiveData<List<ArticleEntity>>

    suspend fun deleteArticle(article: ArticleEntity)

    suspend fun loadNewsFeed(query: String): Flow<PagingData<ArticleEntity>>

    suspend fun searchNews(query: String): Flow<PagingData<ArticleEntity>>
}