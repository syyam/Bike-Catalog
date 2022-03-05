package com.example.bikecatalog.repos

import android.util.Log
import com.example.bikecatalog.api.ApiService
import com.example.bikecatalog.model.BikesResponse
import retrofit2.Response
import javax.inject.Inject

class BikesRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    BikesRepository {
    override suspend fun getSearchResults(): Response<BikesResponse> {
        val res = apiService.getBikes()
        Log.d("aaaaaaaaa", res.message() + res.body())
        return res
    }


}