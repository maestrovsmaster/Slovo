package com.maestrovs.slovo.data.repository

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MainService {

    @GET("swrd")
    suspend fun getSlovo(@Query("swrd") swrd: String) : Response<Any>
}