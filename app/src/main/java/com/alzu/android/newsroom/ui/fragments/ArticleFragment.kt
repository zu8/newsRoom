package com.alzu.android.newsroom.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alzu.android.newsroom.R
import com.alzu.android.newsroom.adapters.pickRandColor
import com.alzu.android.newsroom.data.Article
import com.alzu.android.newsroom.databinding.FragmentArticleBinding
import com.alzu.android.newsroom.ui.NewsRoomActivity
import com.alzu.android.newsroom.utils.Constants.Companion.BOOKMARKS_TAG
import com.alzu.android.newsroom.utils.Constants.Companion.NEWS_TAG
import com.alzu.android.newsroom.utils.Constants.Companion.SEARCH_TAG
import com.bumptech.glide.Glide
import java.lang.Exception

class ArticleFragment : BaseFragment(R.layout.fragment_article) {
    val TAG = "ArticleFragment"
    private val args: ArticleFragmentArgs by navArgs()
    internal var item: Article? = null
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
        val num = args.articleAt
        val source = args.fragmentTag
        when(source) {
            NEWS_TAG -> {
                item = viewModel.breakingNews.value?.data?.articles?.get(num)
                setupFragmentResultAPI(item)
                bindArticle(item)
            }
            SEARCH_TAG -> {
                item = viewModel.searchNews.value?.data?.articles?.get(num)
                setupFragmentResultAPI(item)
                bindArticle(item)
            }
            BOOKMARKS_TAG -> {
                viewModel.getSavedNews().observe(viewLifecycleOwner,Observer { articlesList ->
                    item = articlesList[num]
                    Log.i(TAG,"item: $item")
                    bindArticle(item)
                })
            }
            else -> bindArticle(item)
        }
    }

    private fun setupFragmentResultAPI(item: Article?){
        Log.i(TAG,"$item")
        val bundle = Bundle()
        bundle.putSerializable("article", item)
        requireActivity().supportFragmentManager.setFragmentResult("article_item",bundle)
    }

    private fun bindArticle(item: Article?){
        Log.i(TAG,"begin binding fragment...")
        // binding icon
        item?.iconUrl?.let {
            Glide.with(requireContext()).load(it).into(binding.iconIVStory)
        }
        if (item?.iconUrl == null || item.iconUrl == ""){
            Glide.with(requireContext())
                .load("https://imgholder.ru/70/FF0000/cccccc" +
                        "&text=${item?.source?.name?.get(0)}&font=bebas&kz=72")
                .into(binding.iconIVStory)
        }
        binding.iconIVStory.setOnClickListener {
            val action = ArticleFragmentDirections.actionArticleFragmentToWebArticleFragment(item!!.url)
            findNavController().navigate(action)
        }
        // headline binding
        item?.title?.let{
            binding.headlineTVStory.text = it
        }
        // date binding
        item?.publishedAt?.let{
            binding.dateTVStory.text = it
        }
        // banner binding
        item?.urlToImage?.let{
            try {
                Glide.with(requireContext()).load(it).into(binding.bannerIVStory)
            }
            catch (ex: Exception){
                Glide.with(requireContext())
                    .load("https://imgholder.ru/600x1/8493a8/adb9ca" +
                            "&text=${item?.source?.name}&font=kelson&kz=70")
                    .into(binding.bannerIVStory)
            }
        }
        if (item?.urlToImage == null){
            Glide.with(requireContext())
                .load("https://imgholder.ru/600x300/${pickRandColor()}/cccccc" +
                        "&text=${item?.source?.name}&font=kelson&kz=70")
                .into(binding.bannerIVStory)
        }
        // description binding
        item?.content?.let{
            binding.descriptionTVStory.text = it
        }

    }
}