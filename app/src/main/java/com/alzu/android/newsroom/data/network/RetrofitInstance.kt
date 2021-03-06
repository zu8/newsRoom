package com.alzu.android.newsroom.data.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {

        private const val BASE_URL = "https://newsapi.org/"

        private val retrofit by lazy {

            // create interceptor to insert api-key to all requests
            val interceptor: Interceptor = object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val originalRequest = chain.request()
                    // API_KEY from apikey.properties file
                    val newUrl = originalRequest.url()
                        .newBuilder()
                        .addQueryParameter(
                            "apiKey",
                            com.alzu.android.newsroom.BuildConfig.API_KEY4
                        ).build()
                    val newRequest = originalRequest.newBuilder().url(newUrl).build()
                    Log.i("URL", "$newUrl")
                    return chain.proceed(newRequest)
                }
            }
            val myClient = OkHttpClient().newBuilder()
                .addInterceptor(interceptor)
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(myClient)
                .build()
        }

        val api by lazy {
            retrofit.create(NewsAPI::class.java)
        }
    }

}