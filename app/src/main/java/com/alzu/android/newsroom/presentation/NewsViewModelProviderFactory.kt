package com.alzu.android.newsroom.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alzu.android.newsroom.domain.*
import javax.inject.Inject

class NewsViewModelProviderFactory @Inject constructor(
    private val newsFeed: GetNewsFeedUseCase,
    private val searchFeed: SearchNewsUseCase,
    private val getSavedNewsListUseCase: GetSavedNewsListUseCase,
    private val insertArticleUseCase: InsertArticleUseCase,
    private val deleteArticleUseCase: DeleteArticleUseCase,
    val app: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(
            newsFeed,
            searchFeed,
            getSavedNewsListUseCase,
            insertArticleUseCase,
            deleteArticleUseCase,
            app
        ) as T
    }
}
