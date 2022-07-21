package com.example.pozi_android.data.remote.network

import com.example.pozi_android.data.remote.spec.PBResponce
import com.google.gson.annotations.SerializedName

class PBListRes {
    @SerializedName("items")
    val locations: List<PBResponce> = listOf()
}