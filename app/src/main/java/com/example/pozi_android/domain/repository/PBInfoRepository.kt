package com.example.pozi_android.domain.repository

import com.example.pozi_android.domain.entity.DataResult
import com.example.pozi_android.domain.entity.Place
import com.naver.maps.geometry.LatLng

interface PBInfoRepository {
    suspend fun getPBList(): DataResult<List<Place>>
    suspend fun getPBLocation(latLng: LatLng): DataResult<List<Place>>
}