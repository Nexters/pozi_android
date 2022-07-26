package com.example.pozi_android.data.remote.spec

import com.google.gson.annotations.SerializedName

data class PBRes(
    @SerializedName("items")
    val locations: List<PB> = listOf()
) {
    data class PB(
        val id: Int,
        val address: String,
        val lat: Double,
        val lng: Double,
        val name: String
    )
}