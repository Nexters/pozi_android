package com.example.pozi_android.data.remote.spec

import com.google.gson.annotations.SerializedName

data class TestRes(
    @SerializedName("items")
    val locations: List<Test> = listOf()
) {
    data class Test(
        val id: Int,
        val address: String,
        val lat: Double,
        val lng: Double,
        val name: String
    )
}