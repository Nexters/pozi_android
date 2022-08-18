package com.example.pozi_android.domain.entity

import com.naver.maps.map.overlay.Marker

data class Place(
    var id:Long,
    var marker:Marker,
    var address: String,
    var subject: String,
    var brandName: String
)