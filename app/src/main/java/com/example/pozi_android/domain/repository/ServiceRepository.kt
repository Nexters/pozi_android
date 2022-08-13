package com.example.pozi_android.domain.repository

import com.example.pozi_android.domain.entity.DataResult
import com.example.pozi_android.domain.entity.PBEntity

interface ServiceRepository {
    suspend fun getPBList(): DataResult<List<PBEntity>>
}