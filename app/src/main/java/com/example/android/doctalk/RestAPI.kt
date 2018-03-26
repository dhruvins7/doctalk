package com.example.android.doctalk

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Android on 3/26/2018.
 */
interface RestAPI {

    @GET("search/users")
    fun gitResponse(@Query("q") q: String,
                    @Query("sort") sort: String): Call<Model>
}