package com.alzu.android.newsroom


import android.app.Application
import com.alzu.android.newsroom.di.DaggerApplicationComponent

class NewsRoomApplication: Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}