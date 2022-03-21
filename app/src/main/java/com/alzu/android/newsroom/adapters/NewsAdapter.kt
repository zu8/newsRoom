package com.alzu.android.newsroom.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alzu.android.newsroom.R
import com.alzu.android.newsroom.data.Article
import com.alzu.android.newsroom.data.NewsResponse
import com.alzu.android.newsroom.databinding.ItemArticleBinding
import com.bumptech.glide.Glide
import java.lang.Exception
import java.util.*


class NewsAdapter(private val onClick: (Article) -> Unit):
    RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {
    private val TAG = "NewsAdapter"
    //var resp = NewsResponse(articles = emptyList(), status = "", totalResults = 0) // 1

    inner class ArticleViewHolder(view : View, val onClick: (Article) -> Unit ):
    RecyclerView.ViewHolder(view){
        private val binding = ItemArticleBinding.bind(view)

        private var currentItem: Article? = null
        init{
            binding.iconIVArticle.setClipToOutline(true)
            binding.root.setOnClickListener {
                currentItem?.let {
                    onClick(it)
                }
            }
        }
        fun bind(article:Article) = with (binding){
            currentItem = article
            currentItem?.source?.name?.let{ sourceNameTVArticle.text = it }
            currentItem?.publishedAt?.let{ dateTVArticle.text = it }
            currentItem?.title?.let{ titleTVArticle.text = it }
            currentItem?.urlToImage?.let{
                try {
                    Glide.with(root.context).load(it).into(bannerTVArticle)
                }
                catch (ex: Exception){
                    Glide.with(root.context)
                        .load("https://imgholder.ru/600x300/8493a8/adb9ca" +
                                "&text=${currentItem?.source?.name}&font=kelson&kz=70")
                        .into(bannerTVArticle)
                }
            }
            if (currentItem?.urlToImage == null){
                Glide.with(root.context)
                    .load("https://imgholder.ru/600x300/${pickRandColor()}/cccccc" +
                            "&text=${currentItem?.source?.name}&font=kelson&kz=70")
                    .into(bannerTVArticle)
            }
            currentItem?.description?.let{ descriptionTVArticle.text = it }
            if (currentItem?.description == null){
                descriptionTVArticle.text = ""
            }
            currentItem?.iconUrl?.let {
                Glide.with(root.context).load(it).into(iconIVArticle)
            }
            if (currentItem?.iconUrl == null || currentItem?.iconUrl == ""){
                Glide.with(root.context)
                    .load("https://imgholder.ru/40/FF0000/cccccc" +
                            "&text=${currentItem?.source?.name?.get(0)}&font=roboto&kz=60")
                    .into(iconIVArticle)
            }
            iconIVArticle.setOnClickListener {
                currentItem?.let{
                    Log.i(TAG,"description: ${currentItem!!.description}," +
                            " url to image: ${currentItem!!.urlToImage}, " +
                            " title: ${currentItem!!.title}")
                }
            }
        }
    }

    val differ = AsyncListDiffer(this,ArticleDiffCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_article,
                parent,false
            ),onClick
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        //val article = resp.articles[position]
        val article = differ.currentList[position]
        holder.bind(article)
        Log.i(TAG,"article: $article")
    }

    override fun getItemCount(): Int {
        //return resp.articles.size
        return differ.currentList.size
    }

    /*fun refreshItems(resp: NewsResponse) {
        this.resp = resp
        notifyDataSetChanged()
    }*/

}
private class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {

    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return (oldItem.url == newItem.url)
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return (oldItem == newItem)
    }
}

fun pickRandColor(): String{
    val colors = listOf("9dbf16","ff9948","0082d5","3d4d65","adb9ca")
    val colorNumber = (0..4).random()
    return colors[colorNumber]
}