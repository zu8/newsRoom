package com.alzu.android.newsroom.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alzu.android.newsroom.utils.Constants.Companion.DATABASE_NAME

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDB : RoomDatabase() {
    abstract fun articleDAO(): ArticleDAO

    companion object {
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