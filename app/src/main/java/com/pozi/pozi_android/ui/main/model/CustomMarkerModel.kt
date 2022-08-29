package com.pozi.pozi_android.ui.main.model

import com.naver.maps.map.overlay.Marker

data class CustomMarkerModel(
    var id:Long,
    var marker: Marker,
    var address: String,
    var subject: String,
    var brandName: String,
    var distance: Long
)