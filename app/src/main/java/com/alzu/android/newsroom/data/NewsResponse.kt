package com.alzu.android.newsroom.data


import java.io.Serializable

data class NewsResponse(
    var articles: List<Article>,
    val status: String,
    val totalResults: Int
): Serializable