package com.alzu.android.newsroom.presentation.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alzu.android.newsroom.R
import com.alzu.android.newsroom.presentation.adapters.DefaultLoadStateAdapter
import com.alzu.android.newsroom.presentation.adapters.TryAgainAction
import com.alzu.android.newsroom.databinding.FragmentNewsFeedBinding
import com.alzu.android.newsroom.domain.ArticleEntity
import com.alzu.android.newsroom.presentation.NewsRoomActivity
import com.alzu.android.newsroom.presentation.adapters.NewsAdapter
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NewsFeedFragment : BaseFragment(R.layout.fragment_news_feed) {

    companion object{
        private val TAG = "NewsFeedFragment"
    }

    lateinit var binding: FragmentNewsFeedBinding

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
        setModernRecyclerView()
    }

    private fun hideProgressBar() {
        binding.feedProgressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.feedProgressBar.visibility = View.VISIBLE
    }

    private fun setModernRecyclerView() {
        val newsAdapter = NewsAdapter { item -> adapterOnClick(item) }
        val tryAgainAction: TryAgainAction = { newsAdapter.retry() }
        val footerAdapter = DefaultLoadStateAdapter(tryAgainAction)
        val headerAdapter = DefaultLoadStateAdapter(tryAgainAction)
        val modNewsAdapter = newsAdapter.withLoadStateHeaderAndFooter(headerAdapter, footerAdapter)
        binding.feedRV.apply {
            adapter = modNewsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.errBtn.setOnClickListener { viewModel.refreshFlow()
        }

        newsAdapter.addLoadStateListener { state: CombinedLoadStates ->
            if (state.refresh == LoadState.Loading) showProgressBar()
            if (state.refresh != LoadState.Loading) hideProgressBar()
            binding.errBtn.isVisible = state.refresh is LoadState.Error
        }
        observeArticles(newsAdapter)
    }

    private fun adapterOnClick(item: ArticleEntity) {
        val action = NewsFeedFragmentDirections
            .actionNewsFeedFragmentToArticleFragment(TAG, item)
        findNavController().navigate(action)
    }

    private fun observeArticles(adapter: NewsAdapter) {
        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        lifecycleScope.launch {
            viewModel.news.collectLatest (adapter::submitData)
        }
    }
}