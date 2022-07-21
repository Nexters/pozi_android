package com.example.pozi_android.data.remote.network

import com.example.pozi_android.data.remote.spec.Locations
import com.google.gson.annotations.SerializedName

class LocationRes {
    @SerializedName("items")
    val locations: List<Locations> = listOf()
}