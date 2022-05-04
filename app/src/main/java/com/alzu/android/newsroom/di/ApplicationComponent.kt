package com.alzu.android.newsroom.di

import android.app.Application
import com.alzu.android.newsroom.presentation.NewsRoomActivity
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [DataModule::class]
)
interface ApplicationComponent {

    fun inject(activity: NewsRoomActivity)

    @Component.Factory
    interface Factory{

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}