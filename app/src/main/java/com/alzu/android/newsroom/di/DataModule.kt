package com.alzu.android.newsroom.di

import android.app.Application
import com.alzu.android.newsroom.data.database.ArticleDAO
import com.alzu.android.newsroom.data.database.ArticleDB
import com.alzu.android.newsroom.data.network.NewsAPI
import com.alzu.android.newsroom.data.network.RetrofitInstance
import com.alzu.android.newsroom.data.repository.NewsRepositoryImpl
import com.alzu.android.newsroom.domain.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {
    @Binds
    fun bindNewsRepository(impl: NewsRepositoryImpl): NewsRepository

    companion object{
        @Provides
        fun provideArticleDAO(application: Application): ArticleDAO{
            return ArticleDB(application.applicationContext).articleDAO()
        }

        @Provides
        fun provideArticleApiService(): NewsAPI = RetrofitInstance.api
    }
}