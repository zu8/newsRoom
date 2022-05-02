package com.alzu.android.newsroom.domain

class GetSavedNewsListUseCase(private val repository: NewsRepository) {

    operator fun invoke() = repository.getSavedNews()

}