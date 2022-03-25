package com.alzu.android.newsroom.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alzu.android.newsroom.R
import com.alzu.android.newsroom.data.Article
import com.alzu.android.newsroom.databinding.ItemArticleBinding
import com.bumptech.glide.Glide
import java.lang.Exception

class ModernNewsAdapter(private val onClick: (Article) -> Unit) :
    PagingDataAdapter<Article, ModernNewsAdapter.NewsViewHolder>(ArticleDiffItemCallback) {
    private val TAG = "ModernNewsAdapter"


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ModernNewsAdapter.NewsViewHolder {
        return NewsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_article,
                parent, false
            ), onClick
        )
    }

    override fun onBindViewHolder(holder: ModernNewsAdapter.NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NewsViewHolder(view: View, val onClick: (Article) -> Unit) :
        RecyclerView.ViewHolder(view) {
        private val binding = ItemArticleBinding.bind(view)
        private var currentItem: Article? = null

        init {
            binding.iconIVArticle.setClipToOutline(true)
            binding.root.setOnClickListener {
                currentItem?.let {
                    onClick(it)
                }
            }
        }

        fun bind(article: Article?) = with(binding) {
            currentItem = article
            currentItem?.source?.name?.let { sourceNameTVArticle.text = it }
            currentItem?.publishedAt?.let { dateTVArticle.text = it }
            currentItem?.title?.let { titleTVArticle.text = it }
            currentItem?.urlToImage?.let {
                try {
                    Glide.with(root.context).load(it).into(bannerTVArticle)
                } catch (ex: Exception) {
                    Glide.with(root.context)
                        .load(
                            "https://imgholder.ru/600x300/8493a8/adb9ca" +
                                    "&text=${currentItem?.source?.name}&font=kelson&kz=70"
                        )
                        .into(bannerTVArticle)
                }
            }
            if (currentItem?.urlToImage == null) {
                Glide.with(root.context)
                    .load(
                        "https://imgholder.ru/600x300/${pickRandColorModern()}/cccccc" +
                                "&text=${currentItem?.source?.name}&font=kelson&kz=70"
                    )
                    .into(bannerTVArticle)
            }
            currentItem?.description?.let { descriptionTVArticle.text = it }
            if (currentItem?.description == null) {
                descriptionTVArticle.text = ""
            }
            currentItem?.iconUrl?.let {
                Glide.with(root.context).load(it).into(iconIVArticle)
            }
            if (currentItem?.iconUrl == null || currentItem?.iconUrl == "") {
                Glide.with(root.context)
                    .load(
                        "https://imgholder.ru/40/FF0000/cccccc" +
                                "&text=${currentItem?.source?.name?.get(0)}&font=roboto&kz=60"
                    )
                    .into(iconIVArticle)
            }
            iconIVArticle.setOnClickListener {
                currentItem?.let {
                    Log.i(
                        TAG, "description: ${currentItem!!.description}," +
                                " url to image: ${currentItem!!.urlToImage}, " +
                                " title: ${currentItem!!.title}"
                    )
                }
            }
        }
    }
}

object ArticleDiffItemCallback : DiffUtil.ItemCallback<Article>() {

    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return (oldItem.url == newItem.url)
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return (oldItem == newItem)
    }
}

fun pickRandColorModern(): String {
    val colors = listOf("9dbf16", "ff9948", "0082d5", "3d4d65", "adb9ca")
    val colorNumber = (0..4).random()
    return colors[colorNumber]
}