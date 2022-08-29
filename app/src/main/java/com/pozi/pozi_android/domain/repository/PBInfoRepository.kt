package com.pozi.pozi_android.domain.repository

import com.pozi.pozi_android.domain.entity.DataResult
import com.pozi.pozi_android.domain.entity.PBEntity
import com.naver.maps.geometry.LatLng

interface PBInfoRepository {
    suspend fun getPBLocation(latLng: LatLng): DataResult<List<PBEntity>>
}