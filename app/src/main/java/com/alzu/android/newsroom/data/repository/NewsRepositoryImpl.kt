package com.alzu.android.newsroom.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.*
import com.alzu.android.newsroom.data.database.ArticleDAO
import com.alzu.android.newsroom.data.mapper.ArticleMapper
import com.alzu.android.newsroom.data.network.FeedNewsPagingSource
import com.alzu.android.newsroom.data.network.NewsAPI
import com.alzu.android.newsroom.data.network.SearchNewsPagingSource
import com.alzu.android.newsroom.domain.ArticleEntity
import com.alzu.android.newsroom.domain.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val mapper: ArticleMapper,
    private val articleDAO: ArticleDAO,
    private val apiService: NewsAPI
): NewsRepository {

    override suspend fun insertArticle(article: ArticleEntity) {
        articleDAO.insertArticle(mapper.mapArticleEntityToDbModel(article))
    }

    override fun getSavedNews(): LiveData<List<ArticleEntity>> {
        return Transformations.map(articleDAO.getAllArticles()){ listOfDbModels ->
            listOfDbModels.map {
                mapper.mapArticleDbModelToEntity(it)
            }
        }
    }

    override suspend fun deleteArticle(article: ArticleEntity) {
        articleDAO.deleteArticle(mapper.mapArticleEntityToDbModel(article))
    }

    override suspend fun loadNewsFeed(query: String):Flow<PagingData<ArticleEntity>> {
        return Pager(
            PagingConfig(pageSize = 5),
            pagingSourceFactory = {FeedNewsPagingSource(apiService,query)}
        ).flow
    }

    override suspend fun searchNews(query: String): Flow<PagingData<ArticleEntity>> {
        return Pager(
            PagingConfig(pageSize = 5),
            pagingSourceFactory = { SearchNewsPagingSource(apiService,query) }
        ).flow
    }
}