package com.crypterium.cryptosample.api

import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitBuilder {
    fun newInstance(url: String, merchantId: String, timeoutMillis: Int?): Retrofit {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.writeTimeout(timeoutMillis!!.toLong(), TimeUnit.MILLISECONDS)
        httpClientBuilder.callTimeout(timeoutMillis.toLong(), TimeUnit.MILLISECONDS)
        httpClientBuilder.readTimeout(timeoutMillis.toLong(), TimeUnit.MILLISECONDS)
        httpClientBuilder.connectTimeout(timeoutMillis.toLong(), TimeUnit.MILLISECONDS)
        httpClientBuilder.followRedirects(true)
        httpClientBuilder.followSslRedirects(true)
        httpClientBuilder.addNetworkInterceptor(AuthInterceptor(merchantId))

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClientBuilder.addInterceptor(loggingInterceptor)
        httpClientBuilder.addNetworkInterceptor(StethoInterceptor())

        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClientBuilder.build())
            .build()
    }
}