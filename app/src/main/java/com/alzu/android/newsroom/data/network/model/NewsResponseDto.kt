package com.alzu.android.newsroom.data.network.model


data class NewsResponseDto(
    var articles: List<ArticleDto>,
    val status: String,
    val totalResults: Int
)