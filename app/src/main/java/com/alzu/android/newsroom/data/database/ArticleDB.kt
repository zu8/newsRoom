package com.alzu.android.newsroom.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ArticleDbModel::class],
    version = 1
)

abstract class ArticleDB : RoomDatabase() {
    abstract fun articleDAO(): ArticleDAO

    companion object {

        private const val DATABASE_NAME = "article_db.db"

        @Volatile
        private var instance: ArticleDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDB(context).also { instance = it }
        }

        private fun createDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDB::class.java,
                DATABASE_NAME
            ).build()
    }
}