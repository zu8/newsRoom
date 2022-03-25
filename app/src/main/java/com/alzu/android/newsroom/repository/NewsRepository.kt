package com.alzu.android.newsroom.repository

import com.alzu.android.newsroom.data.Article
import com.alzu.android.newsroom.data.ArticleDB

class NewsRepository(val db: ArticleDB) {

    suspend fun insertArticle(article: Article) = db.articleDAO().insertArticle(article)

    fun getSavedNews() = db.articleDAO().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.articleDAO().deleteArticle(article)
}