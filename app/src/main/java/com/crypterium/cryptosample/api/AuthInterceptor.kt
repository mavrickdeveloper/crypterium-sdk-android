package com.crypterium.cryptosample.api

import com.crypteriumsdk.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthInterceptor internal constructor(private var merchantId: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val builder = original.newBuilder()

        builder.header(BuildConfig.AUTHORIZATION, "Bearer $merchantId")

        builder.method(original.method, original.body)
        val newRequest = builder.build()

        return chain.proceed(newRequest)
    }
}