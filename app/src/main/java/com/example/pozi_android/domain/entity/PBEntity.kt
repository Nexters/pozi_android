package com.example.pozi_android.domain.entity

data class PBEntity(
    val id: Int,
    val address: String,
    val _latitude: Double,
    val _longitude: Double,
    val subject: String,
    val brandName: String,
    val phoneNumber: String
)