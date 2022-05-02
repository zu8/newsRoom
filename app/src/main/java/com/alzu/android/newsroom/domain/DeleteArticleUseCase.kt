package com.alzu.android.newsroom.domain

class DeleteArticleUseCase(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(article: ArticleEntity){
        repository.deleteArticle(article)
    }
}