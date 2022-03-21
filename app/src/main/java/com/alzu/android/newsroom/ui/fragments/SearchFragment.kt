package com.alzu.android.newsroom.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alzu.android.newsroom.R
import com.alzu.android.newsroom.adapters.NewsAdapter
import com.alzu.android.newsroom.data.Article
import com.alzu.android.newsroom.databinding.FragmentSearchBinding
import com.alzu.android.newsroom.utils.Constants.Companion.DELAY_TO_SEARCH
import com.alzu.android.newsroom.utils.Constants.Companion.SEARCH_TAG
import com.alzu.android.newsroom.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment: BaseFragment(R.layout.fragment_search) {
    private val TAG = SEARCH_TAG
    lateinit var binding: FragmentSearchBinding
    lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        hideProgressBar()
        var _job: Job? = null

        binding.searchSV.setOnQueryTextListener( object:
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let{
                    if(it.isNotEmpty()){
                        viewModel.searchNews(it)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                _job?.cancel()
                _job = MainScope()?.launch{
                    delay(DELAY_TO_SEARCH)
                    newText?.let {
                        if(it.isNotEmpty()){
                            viewModel.searchNews(it)
                        }
                    }
                }
                return true
            }

        })

        viewModel.searchNews.observe(viewLifecycleOwner, Observer{ response ->
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
                        Log.i(TAG,it)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        binding.searchProgressBar.visibility = View.GONE
    }
    private fun showProgressBar() {
        binding.searchProgressBar .visibility = View.VISIBLE
    }

    private fun setRecyclerView(){
        newsAdapter = NewsAdapter { item -> adapterOnClick(item) }
        binding.searchRV.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
    private fun adapterOnClick(item: Article) {
        var num = viewModel.searchNews.value?.data?.articles?.indexOf(item)
        if (num == null) num = 0
        val action = SearchFragmentDirections.actionSearchFragmentToArticleFragment(num,TAG)
        findNavController().navigate(action)
        Toast.makeText(requireContext(),"${item.source}", Toast.LENGTH_SHORT).show()
    }
}