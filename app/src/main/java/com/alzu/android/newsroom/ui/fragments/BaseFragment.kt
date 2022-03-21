package com.alzu.android.newsroom.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.alzu.android.newsroom.ui.NewsRoomActivity
import com.alzu.android.newsroom.ui.NewsViewModel

abstract class BaseFragment(layoutId: Int): Fragment(layoutId) {
    lateinit var viewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsRoomActivity).viewModel
    }
}