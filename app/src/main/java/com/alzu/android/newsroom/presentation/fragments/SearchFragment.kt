package com.alzu.android.newsroom.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.alzu.android.newsroom.R
import com.alzu.android.newsroom.presentation.adapters.DefaultLoadStateAdapter
import com.alzu.android.newsroom.presentation.adapters.NewsAdapter
import com.alzu.android.newsroom.presentation.adapters.TryAgainAction
import com.alzu.android.newsroom.databinding.FragmentSearchBinding
import com.alzu.android.newsroom.domain.ArticleEntity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchFragment: BaseFragment(R.layout.fragment_search) {
    companion object{
        const val TAG = "SearchFragment"
    }

    lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setModernRecyclerView()

        binding.searchSV.setOnQueryTextListener( object:
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let{
                    if(it.isNotEmpty()){
                        viewModel.setSearchQuery(it)}
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let{
                    if(it.isNotEmpty()){
                        viewModel.setSearchQuery(it)
                    }
                }
                return true
            }
        })
    }

    private fun hideProgressBar() {
        binding.searchProgressBar.visibility = View.GONE
    }
    private fun showProgressBar() {
        binding.searchProgressBar .visibility = View.VISIBLE
    }

    private fun setModernRecyclerView(){
        val newsAdapter = NewsAdapter { item -> adapterOnClick(item) }
        val tryAgainAction : TryAgainAction = {newsAdapter.retry()}
        val footerAdapter = DefaultLoadStateAdapter(tryAgainAction)
        val headerAdapter = DefaultLoadStateAdapter(tryAgainAction)
        val modNewsAdapter = newsAdapter.withLoadStateHeaderAndFooter(headerAdapter,footerAdapter)
        binding.searchRV.apply {
            adapter = modNewsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        newsAdapter.addLoadStateListener { state: CombinedLoadStates ->
            if (state.refresh ==  LoadState.Loading) showProgressBar()
            if (state.refresh !=  LoadState.Loading) hideProgressBar()
        }
        observeSearchedArticles(newsAdapter)
    }
    private fun adapterOnClick(item: ArticleEntity) {
        val action = SearchFragmentDirections.actionSearchFragmentToArticleFragment(TAG,item)
        findNavController().navigate(action)

    }
    private fun observeSearchedArticles(adapter: NewsAdapter){
        lifecycleScope.launch {
            viewModel.searchResult.collectLatest { pagingData -> adapter.submitData(pagingData) }
        }
    }
}