package com.example.bikecatalog.model

import com.google.gson.annotations.SerializedName

data class BikesResponse(

    @SerializedName("total_count")
    val total: Int = 0,

    @SerializedName("items")
    val items: List<Bikes> = emptyList(),

    val nextPage: Int? = null
)