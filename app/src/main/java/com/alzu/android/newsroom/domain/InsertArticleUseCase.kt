package com.alzu.android.newsroom.domain

import javax.inject.Inject

class InsertArticleUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun  invoke(article: ArticleEntity){
        repository.insertArticle(article)
    }
}