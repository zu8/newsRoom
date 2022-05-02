package com.alzu.android.newsroom.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alzu.android.newsroom.R
import com.alzu.android.newsroom.presentation.adapters.BookmarksAdapter
import com.alzu.android.newsroom.databinding.FragmentBookmarksBinding
import com.alzu.android.newsroom.domain.ArticleEntity
import com.alzu.android.newsroom.presentation.NewsRoomActivity
import com.google.android.material.snackbar.Snackbar

class BookmarksFragment : BaseFragment(R.layout.fragment_bookmarks) {
    companion object{
        const val TAG = "BookmarksFragment"
    }

    lateinit var binding: FragmentBookmarksBinding
    lateinit var bookmarksAdapter: BookmarksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookmarksBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as NewsRoomActivity).checkOut.isVisible = false
        setRecyclerView()

        viewModel.savedArticles.observe(viewLifecycleOwner, Observer { articlesList ->
            bookmarksAdapter.differ.submitList(articlesList)
        })

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = bookmarksAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view, "bookmark deleted", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvBookmarks)
        }
    }

    private fun setRecyclerView() {
        bookmarksAdapter = BookmarksAdapter { item -> adapterOnClick(item) }
        binding.rvBookmarks.apply {
            adapter = bookmarksAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun adapterOnClick(item: ArticleEntity) {
        val action = BookmarksFragmentDirections.actionBookmarksFragmentToArticleFragment(TAG, item)
        findNavController().navigate(action)
    }
}