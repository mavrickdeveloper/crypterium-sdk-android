package com.crypterium.cryptosample.api

interface ApiCallback {

    fun onError(error: Throwable)

    fun <T> onSuccess(response: T)
}