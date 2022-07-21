package com.example.pozi_android.data.remote.spec

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Locations(
    @SerializedName("id") //이거는 받아오는 순서대로 넣어주는 라이브러리 사용하기
    val id: Int,

    @SerializedName("address")
    val address: String,

    @SerializedName("lat")
    val lat: Double,

    @SerializedName("lng")
    val lng: Double,

    @SerializedName("name")
    val name: String
)