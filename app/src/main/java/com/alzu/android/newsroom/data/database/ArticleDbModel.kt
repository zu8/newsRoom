package com.alzu.android.newsroom.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "articles"
)
data class ArticleDbModel(
var iconUrl: String? = null,
var author: String? = null,
var content: String? = null,
var description: String? = null,
val publishedAt: String,
val sourceName: String?,
val title: String,
@PrimaryKey
val url: String,
var urlToImage: String? = null
)

