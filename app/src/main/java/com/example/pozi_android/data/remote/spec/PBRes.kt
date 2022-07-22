package com.example.pozi_android.data.remote.spec

data class PBRes(
    var adress: String? = null,
    var coordinates: Map<String, Double>? = null,
    var phoneNumber: String? = null,
    var subject: String? = null
)