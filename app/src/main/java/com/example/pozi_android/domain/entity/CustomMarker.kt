package com.example.pozi_android.domain.entity

import com.naver.maps.map.overlay.Marker

data class CustomMarker(
val marker:Marker,
val address: String,
val subject: String,
val brandName: String,
val phoneNumber: String
)