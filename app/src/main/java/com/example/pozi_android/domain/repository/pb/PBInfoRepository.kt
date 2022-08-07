package com.example.pozi_android.domain.repository.pb

import com.example.pozi_android.data.remote.network.DataResult
import com.example.pozi_android.domain.entity.PBEntity

interface PBInfoRepository {
    suspend fun getPBList(): DataResult<List<PBEntity>>
}