package com.example.pozi_android.domain.repository

import com.example.pozi_android.data.remote.network.DataResult
import com.example.pozi_android.data.remote.network.PBListRes
import com.example.pozi_android.domain.entity.PB

interface ServiceRepository {
    suspend fun getPBList(): DataResult<List<PB>>
}