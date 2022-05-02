package com.alzu.android.newsroom.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alzu.android.newsroom.R
import com.alzu.android.newsroom.presentation.adapters.pickRandColorModern
import com.alzu.android.newsroom.databinding.FragmentArticleBinding
import com.alzu.android.newsroom.domain.ArticleEntity
import com.alzu.android.newsroom.presentation.NewsRoomActivity
import com.bumptech.glide.Glide
import java.lang.Exception

class ArticleFragment : BaseFragment(R.layout.fragment_article) {

    companion object{
        const val TAG = "ArticleFragment"
        const val NEWS_TAG = "NewsFeedFragment"
        const val SEARCH_TAG = "SearchFragment"
    }

    private val args: ArticleFragmentArgs by navArgs()
    internal var item: ArticleEntity? = null
    lateinit var binding: FragmentArticleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as NewsRoomActivity).checkOut.isVisible = true
        item = args.article
        val source = args.fragmentTag
        if (source == NEWS_TAG || source == SEARCH_TAG) setupFragmentResultAPI(item)
        bindArticle(item)
    }

    private fun setupFragmentResultAPI(item: ArticleEntity?) {
        Log.i(TAG, "$item")
        val bundle = Bundle()
        bundle.putSerializable("article", item)
        requireActivity().supportFragmentManager.setFragmentResult("article_item", bundle)
    }

    private fun bindArticle(item: ArticleEntity?) {
        Log.i(TAG, "begin binding fragment...")
        // binding icon
        item?.iconUrl?.let {
            Glide.with(requireContext()).load(it).into(binding.iconIVStory)
        }
        if (item?.iconUrl == null || item.iconUrl == "") {
            Glide.with(requireContext())
                .load(
                    "https://imgholder.ru/70/FF0000/cccccc" +
                            "&text=${item?.sourceName?.get(0)}&font=bebas&kz=72"
                )
                .into(binding.iconIVStory)
        }
        binding.iconIVStory.setOnClickListener {
            val action =
                ArticleFragmentDirections.actionArticleFragmentToWebArticleFragment(item!!.url)
            findNavController().navigate(action)
        }
        // headline binding
        item?.title?.let {
            binding.headlineTVStory.text = it
        }
        // date binding
        item?.publishedAt?.let {
            binding.dateTVStory.text = it
        }
        // banner binding
        item?.urlToImage?.let {
            try {
                Glide.with(requireContext()).load(it).into(binding.bannerIVStory)
            } catch (ex: Exception) {
                Glide.with(requireContext())
                    .load(
                        "https://imgholder.ru/600x1/8493a8/adb9ca" +
                                "&text=${item.sourceName}&font=kelson&kz=70"
                    )
                    .into(binding.bannerIVStory)
            }
        }
        if (item?.urlToImage == null) {
            Glide.with(requireContext())
                .load(
                    "https://imgholder.ru/600x300/${pickRandColorModern()}/cccccc" +
                            "&text=${item?.sourceName}&font=kelson&kz=70"
                )
                .into(binding.bannerIVStory)
        }
        // description binding
        item?.content?.let {
            binding.descriptionTVStory.text = it
        }

    }
}