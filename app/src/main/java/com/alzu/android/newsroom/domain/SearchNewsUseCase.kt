package com.alzu.android.newsroom.domain

import javax.inject.Inject


class SearchNewsUseCase @Inject constructor(
    private val repository: NewsRepository,
) {
    suspend operator fun invoke(query: String) = repository.searchNews(query)
}