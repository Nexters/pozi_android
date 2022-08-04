package com.example.pozi_android.domain.repository

import com.example.pozi_android.domain.entity.DataResult
import com.example.pozi_android.domain.entity.PB

interface PBInfoRepository {
    suspend fun getPBList(): DataResult<List<PB>>
}