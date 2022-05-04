package com.alzu.android.newsroom.domain

import javax.inject.Inject

class GetSavedNewsListUseCase @Inject constructor
    (private val repository: NewsRepository) {

    operator fun invoke() = repository.getSavedNews()

}