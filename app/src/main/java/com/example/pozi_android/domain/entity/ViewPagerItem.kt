package com.example.pozi_android.domain.entity

import com.naver.maps.map.overlay.Marker

data class ViewPagerItem(
    val place: Place,
    var distance: Long?
)