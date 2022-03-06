package com.example.bikecatalog.api

import com.example.bikecatalog.model.Bikes
import com.example.bikecatalog.model.BikesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    companion object {
        const val BASE_URL = "https://api.jsonbin.io/"
    }
    @GET("b/621f6120a703bb6749204708/4")
    suspend fun getBikes(): Response<BikesResponse>
}