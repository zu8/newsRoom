package com.alzu.android.newsroom.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.alzu.android.newsroom.presentation.NewsRoomActivity
import com.alzu.android.newsroom.presentation.NewsViewModel

abstract class BaseFragment(layoutId: Int): Fragment(layoutId) {
    lateinit var viewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsRoomActivity).viewModel
    }
}