package com.alzu.android.newsroom.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.alzu.android.newsroom.R

class WebArticleFragment: BaseFragment(R.layout.fragment_web_article) {
    private val args: WebArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = args.storyUrl
        val webViewPlaceholder = view.findViewById<WebView>(R.id.webView)
        webViewPlaceholder.apply {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled
            loadUrl(url)
        }
    }
}