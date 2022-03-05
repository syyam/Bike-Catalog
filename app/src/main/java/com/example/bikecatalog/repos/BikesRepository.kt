package com.example.bikecatalog.repos

import com.example.bikecatalog.model.BikesResponse
import retrofit2.Response


interface BikesRepository {
    suspend fun getSearchResults(): Response<BikesResponse>

}