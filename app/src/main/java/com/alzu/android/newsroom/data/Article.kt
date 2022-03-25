package com.alzu.android.newsroom.data


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "articles"
)

data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var iconUrl: String? = null,
    var author: String? = null,
    var content: String? = null,
    var description: String? = null,
    val publishedAt: String,
    val source: Source?,
    val title: String,
    val url: String,
    var urlToImage: String? = null
) : Serializable