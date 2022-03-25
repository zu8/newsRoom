package com.alzu.android.newsroom.ui.fragments

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
import com.alzu.android.newsroom.adapters.BookmarksAdapter
import com.alzu.android.newsroom.data.Article
import com.alzu.android.newsroom.databinding.FragmentBookmarksBinding
import com.alzu.android.newsroom.ui.NewsRoomActivity
import com.alzu.android.newsroom.utils.Constants.Companion.BOOKMARKS_TAG
import com.google.android.material.snackbar.Snackbar

class BookmarksFragment : BaseFragment(R.layout.fragment_bookmarks) {
    private val TAG = BOOKMARKS_TAG
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

        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer { articlesList ->
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

    private fun adapterOnClick(item: Article) {
        val action = BookmarksFragmentDirections.actionBookmarksFragmentToArticleFragment(TAG, item)
        findNavController().navigate(action)
    }

}