package com.example.pozi_android.domain.usecase

import com.example.pozi_android.domain.entity.DataResult
import com.example.pozi_android.domain.entity.PBEntity
import com.example.pozi_android.domain.repository.PBInfoRepository
import com.naver.maps.geometry.LatLng
import javax.inject.Inject

class GetPhotoBoothListLocationUseCase @Inject constructor(
    private val repository: PBInfoRepository
) {
    suspend operator fun invoke(latLng: LatLng): DataResult<List<PBEntity>> {
        return repository.getPBLocation(latLng)
    }
}