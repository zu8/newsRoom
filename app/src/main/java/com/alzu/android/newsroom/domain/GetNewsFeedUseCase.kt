package com.alzu.android.newsroom.domain

import javax.inject.Inject


class GetNewsFeedUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(query: String) = repository.loadNewsFeed(query)

}