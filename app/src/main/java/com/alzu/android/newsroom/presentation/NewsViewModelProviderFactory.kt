package com.alzu.android.newsroom.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NewsViewModelProviderFactory(
    val app: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app) as T
    }
}