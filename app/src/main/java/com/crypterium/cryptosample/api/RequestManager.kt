package com.crypterium.cryptosample.api

import com.crypteriumsdk.screens.common.domain.ApiError
import com.crypteriumsdk.screens.common.domain.CommonErrorObserver
import com.crypteriumsdk.screens.common.domain.JICommonNetworkErrorResponse
import com.crypteriumsdk.screens.common.domain.JICommonNetworkResponse
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class RequestManager constructor(url: String){

    private var compositeDisposable = CompositeDisposable()
    var merchantId: String = "4bde0e39-58b9-484e-b4a6-26008f37178d"
    var api: ApiInterfaces? = null

    init {
        reInit(url)
    }

    fun reInit(url: String){
        api = RetrofitBuilder.newInstance(url, merchantId, 60000)
            .create(com.crypterium.cryptosample.api.ApiInterfaces::class.java)
    }

    fun auth(customerId: String, callback: JICommonNetworkResponse<AuthResponse>?,
             errorCallback: JICommonNetworkErrorResponse?){
        val body = JsonObject()
        body.addProperty("customerId", customerId)

        sendRequest<AuthResponse>(api!!.auth(body), callback, errorCallback)
    }

    protected fun <T> sendRequest(
        observable: Observable<T>,
        callback: JICommonNetworkResponse<T>? = null,
        errorCallback: JICommonNetworkErrorResponse? = null,
        observer: CommonErrorObserver<T> = getObserver(callback, errorCallback)
    ) {
        val disposable = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(observer)

        addDisposable(disposable)
    }

    private fun addDisposable(disposable: Disposable) {
        if (compositeDisposable.isDisposed) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable.add(disposable)
    }

    private fun <T> getObserver(
        callback: JICommonNetworkResponse<T>?,
        errorCallback: JICommonNetworkErrorResponse?
    ): CommonErrorObserver<T> {

        return object : CommonErrorObserver<T>() {

            override fun onNext(t: T) {
                callback?.onResponseSuccess(t)
            }

            override fun onError(e: Throwable) {
                val error = ApiError(e)
                errorCallback?.onResponseError(error)
            }

            override fun onComplete() {
            }
        }
    }
}