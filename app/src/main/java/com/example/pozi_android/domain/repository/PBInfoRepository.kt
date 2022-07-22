package com.example.pozi_android.domain.repository

import com.example.pozi_android.data.remote.network.DataResult
import com.example.pozi_android.data.remote.spec.PBRes
import com.example.pozi_android.domain.entity.PB

interface PBInfoRepository {
    suspend fun getPBList(): DataResult<List<PB>>
}