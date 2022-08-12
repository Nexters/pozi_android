package com.example.pozi_android.domain.usecase

import com.example.pozi_android.domain.entity.DataResult
import com.example.pozi_android.domain.entity.PBEntity
import com.example.pozi_android.domain.repository.PBInfoRepository
import javax.inject.Inject

class GetPhotoBoothListUseCase @Inject constructor(
    private val repository: PBInfoRepository
) {
    suspend operator fun invoke(): DataResult<List<PBEntity>> {
        return repository.getPBList()
    }
}