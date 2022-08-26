package com.liyaan.test

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Method
import java.util.concurrent.TimeUnit


private const val DEFAULT_TIME:Long = 30
var logInterceptor = HttpLoggingInterceptor(HttpLogger())

object Api {
    private val okHttpClient by lazy {
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder().connectTimeout(DEFAULT_TIME, TimeUnit.SECONDS)//设置请求超时时间
            .readTimeout(DEFAULT_TIME, TimeUnit.SECONDS)//设置读取超时时间
            .addNetworkInterceptor(logInterceptor)
            .writeTimeout(DEFAULT_TIME, TimeUnit.SECONDS)//设置写入超时时间
            .retryOnConnectionFailure(true).build()//设置出现错误进行重新连接
    }
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.wanandroid.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

//            .callFactory(object: CallFactoryProxy(okHttpClient) {
//                override fun getNewUrl(baseUrlName: String?, request: Request?): HttpUrl? {
//                    Log.i("baseUrl",baseUrlName!!)
//                    if (baseUrlName == "www.wanandroid.com") {
//                        val oldUrl = request!!.url.toString()
//                        val newUrl =
//                            oldUrl.replace("https://wanandroid.com/", "https://www.baidu.com/")
//                        return newUrl.
//                    }
//                    return null
//                }

//            })
    }
    private val api by lazy {
        retrofit.create(IApi::class.java)
    }
    fun get(): IApi {

        return api
    }
    fun testRetrofit(){

        val url:String = MvpSpUtils.getString("base_url")
        if (url.isNotEmpty()){
            val retrofit = retrofit::class.java

            val baseUrlField = retrofit.getDeclaredField("baseUrl")
            baseUrlField.isAccessible = true

            val mRetrofit = Api.retrofit
            val nerBaseUrl = Retrofit.Builder().baseUrl(url).build().baseUrl()

            baseUrlField.set(mRetrofit, nerBaseUrl)

            val serviceMethodCacheField = retrofit.getDeclaredField("serviceMethodCache")
            serviceMethodCacheField.isAccessible = true

            val serviceMethodCache: MutableMap<Method, Any> = serviceMethodCacheField[Api.retrofit] as MutableMap<Method, Any>
            serviceMethodCache.clear()
            serviceMethodCacheField.set(mRetrofit, serviceMethodCache)
        }

    }
}