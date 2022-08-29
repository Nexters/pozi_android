package com.pozi.pozi_android.data.remote.spec

import com.google.gson.annotations.SerializedName

data class PBRes(
    @SerializedName("result")
    val result: List<PB> = listOf()
){
    data class PB(
        var brandName: String,
        var subject: String,
        var coordinates: Map<String, Double>,
        var phoneNumber: String,
        var address: String,
    )
}