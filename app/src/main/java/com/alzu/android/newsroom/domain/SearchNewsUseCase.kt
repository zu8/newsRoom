package com.alzu.android.newsroom.domain


class SearchNewsUseCase(
    private val repository: NewsRepository,
) {
    suspend operator fun invoke(query: String) = repository.searchNews(query)
}