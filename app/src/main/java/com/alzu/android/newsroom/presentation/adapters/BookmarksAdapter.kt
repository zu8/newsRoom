package com.alzu.android.newsroom.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alzu.android.newsroom.R
import com.alzu.android.newsroom.databinding.ItemBookmarkBinding
import com.alzu.android.newsroom.domain.ArticleEntity
import com.bumptech.glide.Glide
import java.lang.Exception

class BookmarksAdapter(private val onClick: (ArticleEntity) -> Unit) :
    RecyclerView.Adapter<BookmarksAdapter.BookmarkViewHolder>() {
    private val TAG = "BookmarksAdapter"

    inner class BookmarkViewHolder(view: View, val onClick: (ArticleEntity) -> Unit) :
        RecyclerView.ViewHolder(view) {
        private val binding = ItemBookmarkBinding.bind(view)
        private var currentItem: ArticleEntity? = null

        init {
            binding.iconIV.clipToOutline = true
            binding.root.setOnClickListener {
                currentItem?.let {
                    onClick(it)
                }
            }
        }

        fun bind(article: ArticleEntity) = with(binding) {
            currentItem = article
            //binding text fields
            currentItem?.sourceName?.let { sourceNameTV.text = it }
            currentItem?.title?.let { titleTV.text = it }
            currentItem?.publishedAt?.let { dateTV.text = it }
            //placing banner-picture
            currentItem?.urlToImage?.let {
                try {
                    Glide.with(root.context).load(it).into(articleIV)
                } catch (ex: Exception) {
                    Glide.with(root.context)
                        .load(
                            "https://imgholder.ru/100x100/8493a8/adb9ca" +
                                    "&text=${currentItem?.sourceName}&font=kelson&kz=70"
                        )
                        .into(articleIV)
                }
            }
            if (currentItem?.urlToImage == null) {
                Glide.with(root.context)
                    .load(
                        "https://imgholder.ru/100x100/${pickRandColorBookmark()}/cccccc" +
                                "&text=${currentItem?.sourceName}&font=kelson&kz=70"
                    )
                    .into(articleIV)
            }
            // placing source-icon
            currentItem?.iconUrl?.let {
                try {
                    Glide.with(root.context).load(it).into(iconIV)
                } catch (ex: Exception) {
                    Glide.with(root.context)
                        .load(
                            "https://imgholder.ru/48/FF0000/cccccc" +
                                    "&text=${currentItem?.sourceName}&font=kelson&kz=70"
                        )
                        .into(iconIV)
                }
            }
            if (currentItem?.iconUrl == null) {
                Glide.with(root.context)
                    .load(
                        "https://imgholder.ru/48/FF0000/cccccc" +
                                "&text=${currentItem?.sourceName?.get(0)}&font=roboto&kz=60"
                    )
                    .into(iconIV)
            }

        }

        private fun pickRandColorBookmark(): String {
            val colors = listOf("9dbf16", "ff9948", "0082d5", "3d4d65", "adb9ca")
            val colorNumber = (0..4).random()
            return colors[colorNumber]
        }

    }

    val differ = AsyncListDiffer(this, BookmarksDiffCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        return BookmarkViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_bookmark,
                    parent, false
                ), onClick
        )
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}

private class BookmarksDiffCallback : DiffUtil.ItemCallback<ArticleEntity>() {

    override fun areItemsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean {
        return (oldItem.url == newItem.url)
    }

    override fun areContentsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean {
        return (oldItem == newItem)
    }
}
