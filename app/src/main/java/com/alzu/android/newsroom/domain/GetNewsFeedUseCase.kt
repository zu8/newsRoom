package com.alzu.android.newsroom.domain


class GetNewsFeedUseCase(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(query: String) = repository.loadNewsFeed(query)

}