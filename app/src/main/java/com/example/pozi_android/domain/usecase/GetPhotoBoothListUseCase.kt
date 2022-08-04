package com.example.pozi_android.domain.usecase

import com.example.pozi_android.data.repository.pb.PBInfoRepositoryImpl
import com.example.pozi_android.domain.entity.DataResult
import com.example.pozi_android.domain.entity.PB
import javax.inject.Inject

class GetPhotoBoothListUseCase @Inject constructor(
    private val repository: PBInfoRepositoryImpl
) {
    suspend operator fun invoke(): DataResult<List<PB>> {
        return repository.getPBList()
    }
}