package com.fjji.gifs_fjji_can_do.API.networking

import com.fjji.gifs_fjji_can_do.API.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiGifQuery {
    @GET("search")
    fun getGif(
        @Header("api_key") api_key: String,
        @Query("q") q: String,
        @Query("limit") limit: Int?,
        @Query("rating") rating: List<String>
    ): Call<ManyGifs>

    @GET("random")
    fun getRandomGif(
        @Header("api_key") api_key: String,
        @Query("rating") rating: List<String>
    ): Call<RandGif>
}
interface Categories{
    @GET("categories")
    fun ApiCategories(@Query("api_key") api_key:String): Call<ListGifCategories>
}
