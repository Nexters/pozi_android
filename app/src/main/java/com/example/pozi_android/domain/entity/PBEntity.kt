package com.example.pozi_android.domain.entity

data class PBEntity(
    val coordinates: Map<String, Double>,
    val address: String,
    val subject: String,
    val brandName: String,
    var distance: Long
)
