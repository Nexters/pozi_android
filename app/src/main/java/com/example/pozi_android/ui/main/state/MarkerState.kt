package com.example.pozi_android.ui.main.state

import com.example.pozi_android.domain.entity.CustomMarker

sealed class MarkerState {
    object NoData : MarkerState() //아무것도 없을때
    data class On(val prev: CustomMarker) : MarkerState() //처음 마커가 선택될때
    data class Off(val prev: CustomMarker) : MarkerState() //마커가 변경될 때
    data class Clear(val prev: CustomMarker) : MarkerState() //처음 마커가 선택될때
}
