package com.alzu.android.newsroom.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alzu.android.newsroom.R
import com.alzu.android.newsroom.adapters.NewsAdapter
import com.alzu.android.newsroom.data.Article
import com.alzu.android.newsroom.databinding.FragmentNewsFeedBinding
import com.alzu.android.newsroom.ui.NewsRoomActivity
import com.alzu.android.newsroom.utils.Constants.Companion.NEWS_TAG
import com.alzu.android.newsroom.utils.Resource

class NewsFeedFragment: BaseFragment(R.layout.fragment_news_feed) {
    private val TAG = NEWS_TAG
    lateinit var binding: FragmentNewsFeedBinding
    lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsFeedBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as NewsRoomActivity).checkOut.isVisible = false
        setRecyclerView()

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer{ response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let{
                        newsAdapter.differ.submitList(it.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let{
                        Log.i("Err_feed",it)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        binding.feedProgressBar.visibility = View.GONE
    }
    private fun showProgressBar() {
        binding.feedProgressBar.visibility = View.VISIBLE
    }

    private fun setRecyclerView(){
        newsAdapter = NewsAdapter { item -> adapterOnClick(item) }
        binding.feedRV.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
    private fun adapterOnClick(item: Article) {
        var num = viewModel.breakingNews.value?.data?.articles?.indexOf(item)
        if (num == null) num = 0
        val action = NewsFeedFragmentDirections
            .actionNewsFeedFragmentToArticleFragment(num, TAG)
        findNavController().navigate(action)
        Log.i(TAG, viewModel.breakingNews.value?.data.toString())
    }
}