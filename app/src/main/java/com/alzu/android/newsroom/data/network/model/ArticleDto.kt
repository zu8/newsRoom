package com.alzu.android.newsroom.data.network.model


data class ArticleDto(
    var iconUrl: String? = null,
    var author: String? = null,
    var content: String? = null,
    var description: String? = null,
    val publishedAt: String,
    val source: SourceDto?,
    val title: String,
    val url: String,
    var urlToImage: String? = null
)