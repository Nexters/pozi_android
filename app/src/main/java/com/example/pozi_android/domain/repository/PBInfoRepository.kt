package com.example.pozi_android.domain.repository

import com.example.pozi_android.domain.entity.DataResult
import com.example.pozi_android.domain.entity.PBEntity

interface PBInfoRepository {
    suspend fun getPBList(): DataResult<List<PBEntity>>
}