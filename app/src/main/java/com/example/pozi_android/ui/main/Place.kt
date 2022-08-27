package com.example.pozi_android.ui.main

import com.naver.maps.map.overlay.Marker

data class Place(
    var id:Long,
    var marker: Marker,
    var address: String,
    var subject: String,
    var brandName: String,
    var distance: Long
)