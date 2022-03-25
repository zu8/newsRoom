package com.alzu.android.newsroom.ui.fragments

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
import com.alzu.android.newsroom.adapters.DefaultLoadStateAdapter
import com.alzu.android.newsroom.adapters.ModernNewsAdapter
import com.alzu.android.newsroom.adapters.TryAgainAction
import com.alzu.android.newsroom.data.Article
import com.alzu.android.newsroom.databinding.FragmentNewsFeedBinding
import com.alzu.android.newsroom.ui.NewsRoomActivity
import com.alzu.android.newsroom.utils.Constants.Companion.NEWS_TAG
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NewsFeedFragment : BaseFragment(R.layout.fragment_news_feed) {

    private val TAG = NEWS_TAG
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
        val newsAdapter = ModernNewsAdapter { item -> adapterOnClick(item) }
        val tryAgainAction: TryAgainAction = { newsAdapter.retry() }
        val footerAdapter = DefaultLoadStateAdapter(tryAgainAction)
        val headerAdapter = DefaultLoadStateAdapter(tryAgainAction)
        val modNewsAdapter = newsAdapter.withLoadStateHeaderAndFooter(headerAdapter, footerAdapter)
        binding.feedRV.apply {
            adapter = modNewsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.errBtn.setOnClickListener { viewModel.refreshFlow() }

        newsAdapter.addLoadStateListener { state: CombinedLoadStates ->
            if (state.refresh == LoadState.Loading) showProgressBar()
            if (state.refresh != LoadState.Loading) hideProgressBar()
            binding.errBtn.isVisible = state.refresh is LoadState.Error
        }
        observeArticles(newsAdapter)
    }

    private fun adapterOnClick(item: Article) {
        val action = NewsFeedFragmentDirections
            .actionNewsFeedFragmentToArticleFragment(TAG, item)
        findNavController().navigate(action)
    }

    private fun observeArticles(adapter: ModernNewsAdapter) {
        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        lifecycleScope.launch {
            viewModel.breakingNewsFlow.collectLatest(adapter::submitData)
        }

    }
}