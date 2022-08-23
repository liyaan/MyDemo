package com.liyaan.test

import retrofit2.http.GET

interface IApi {
    @GET("banner/json")
    suspend fun getBanner(): ResponseResult<List<Banner>>
}