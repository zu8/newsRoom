package com.alzu.android.newsroom.data.mapper

import com.alzu.android.newsroom.data.database.ArticleDbModel
import com.alzu.android.newsroom.data.network.model.ArticleDto
import com.alzu.android.newsroom.domain.ArticleEntity
import javax.inject.Inject

class ArticleMapper @Inject constructor() {


    fun mapArticleDbModelToEntity( articleDbModel: ArticleDbModel): ArticleEntity{
        return ArticleEntity(
            iconUrl = articleDbModel.iconUrl,
        author = articleDbModel.author,
        content = articleDbModel.content,
        description = articleDbModel.description,
        publishedAt = articleDbModel.publishedAt,
        sourceName = articleDbModel.sourceName,
        title = articleDbModel.title,
        url = articleDbModel.url,
        urlToImage = articleDbModel.urlToImage
        )
    }

    fun mapArticleEntityToDbModel( articleEntity: ArticleEntity): ArticleDbModel{
        return ArticleDbModel(
            iconUrl = articleEntity.iconUrl,
            author = articleEntity.author,
            content = articleEntity.content,
            description = articleEntity.description,
            publishedAt = articleEntity.publishedAt,
            sourceName = articleEntity.sourceName,
            title = articleEntity.title,
            url = articleEntity.url,
            urlToImage = articleEntity.urlToImage
        )
    }

    fun mapArticleDtoToEntity( articleDto: ArticleDto): ArticleEntity{
        return ArticleEntity(
            iconUrl = articleDto.iconUrl,
            author = articleDto.author,
            content = articleDto.content,
            description = articleDto.description,
            publishedAt = articleDto.publishedAt,
            sourceName = articleDto.source?.name,
            title = articleDto.title,
            url = articleDto.url,
            urlToImage = articleDto.urlToImage
        )
    }
}