package com.example.pozi_android.domain.repository

import com.example.pozi_android.domain.entity.DataResult
import com.example.pozi_android.domain.entity.PBEntity
import com.example.pozi_android.domain.entity.Place
import com.naver.maps.geometry.LatLng

interface PBInfoRepository {
    suspend fun getPBLocation(latLng: LatLng): DataResult<List<PBEntity>>
}