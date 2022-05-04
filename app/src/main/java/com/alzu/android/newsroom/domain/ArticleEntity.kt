package com.alzu.android.newsroom.domain

import java.io.Serializable

data class ArticleEntity(
    val iconUrl: String?,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String,
    val sourceName: String?,
    val title: String,
    val url: String,
    var urlToImage: String?,
): Serializable