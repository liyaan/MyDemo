package com.liyaan.test

import android.util.Log
import androidx.annotation.Nullable
import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.Request


abstract class CallFactoryProxy(delegate: Call.Factory) : Call.Factory {
    private val delegate: Call.Factory
    private val TAG = "CallFactoryProxy"
    override fun newCall(request: Request): Call {
        val baseUrlName: String? = request.header(NAME_BASE_URL)
        if (baseUrlName != null) {
            val newHttpUrl = getNewUrl(baseUrlName, request)
            if (newHttpUrl != null) {
                val newRequest: Request = request.newBuilder().url(newHttpUrl).build()
                return delegate.newCall(newRequest)
            } else {
                Log.w(TAG, "getNewUrl() return null when baseUrlName==$baseUrlName")
            }
        }
        return delegate.newCall(request)
    }

    @Nullable
    protected abstract fun getNewUrl(baseUrlName: String?, request: Request?): HttpUrl?

    companion object {
        private const val NAME_BASE_URL = "BaseUrlName"
    }

    init {
        this.delegate = delegate
    }
}