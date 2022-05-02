package com.alzu.android.newsroom.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ArticleDAO {
    @Query("SELECT * FROM articles ORDER BY publishedAt DESC")
    fun getAllArticles(): LiveData<List<ArticleDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: ArticleDbModel): Long

    @Delete
    suspend fun deleteArticle(article: ArticleDbModel)
}