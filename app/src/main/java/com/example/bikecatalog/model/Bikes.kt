package com.example.bikecatalog.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Bikes(

    @SerializedName("id")
    val id: Long,

    @SerializedName("bike_name")
    val bikeName: String,

    @SerializedName("category")
    val category: String,

    @SerializedName("frame_size")
    val frameSize: String?,

    @SerializedName("price")
    val price: Int?,

    @SerializedName("picture")
    val picture: String,

) : Parcelable