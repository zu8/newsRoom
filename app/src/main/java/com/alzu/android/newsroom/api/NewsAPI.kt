package com.alzu.android.newsroom.api

import com.alzu.android.newsroom.data.NewsResponse
import com.alzu.android.newsroom.utils.Constants.Companion.API_KEY2
import com.alzu.android.newsroom.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "ru",
        @Query("page")
        pageNum: Int = 1
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNum: Int = 1
    ): Response<NewsResponse>
}