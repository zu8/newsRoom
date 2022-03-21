package com.alzu.android.newsroom.repository

import com.alzu.android.newsroom.api.RetrofitInstance
import com.alzu.android.newsroom.data.Article
import com.alzu.android.newsroom.data.ArticleDB
import com.alzu.android.newsroom.data.NewsResponse
import retrofit2.Response

class NewsRepository( val db: ArticleDB) {

    suspend fun getBreakingNews(countryCode: String,numOfPages: Int): Response<NewsResponse>{
        return RetrofitInstance.api.getBreakingNews(countryCode,numOfPages)
    }

    suspend fun searchNews(searchQuery: String, pageNum: Int): Response<NewsResponse>{
        return RetrofitInstance.api.searchForNews(searchQuery,pageNum)
    }

    suspend fun insertArticle(article: Article) = db.articleDAO().insertArticle(article)

    fun getSavedNews() = db.articleDAO().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.articleDAO().deleteArticle(article)
}