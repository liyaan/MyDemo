package com.liyaan.test

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val DEFAULT_TIME:Long = 30
object Api {
    private val okHttpClient by lazy {
        OkHttpClient.Builder().connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS)//设置请求超时时间
            .readTimeout(DEFAULT_TIME, TimeUnit.SECONDS)//设置读取超时时间
            .writeTimeout(DEFAULT_TIME, TimeUnit.SECONDS)//设置写入超时时间
            .retryOnConnectionFailure(true).build()//设置出现错误进行重新连接
    }
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.wanandroid.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
    private val api by lazy {
        retrofit.create(IApi::class.java)
    }
    fun get(): IApi {
        return api
    }
}