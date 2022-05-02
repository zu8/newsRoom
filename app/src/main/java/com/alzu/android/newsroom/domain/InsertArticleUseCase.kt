package com.alzu.android.newsroom.domain

class InsertArticleUseCase(
    private val repository: NewsRepository
) {
    suspend operator fun  invoke(article: ArticleEntity){
        repository.insertArticle(article)
    }
}