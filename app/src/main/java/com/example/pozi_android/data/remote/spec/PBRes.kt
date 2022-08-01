package com.example.pozi_android.data.remote.spec

data class PBRes(
    var address: String,
    var coordinates: Map<String, Double>,
    var phoneNumber: String,
    var subject: String,
    var brandName: String
)