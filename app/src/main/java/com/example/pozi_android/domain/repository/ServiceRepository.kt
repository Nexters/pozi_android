package com.example.pozi_android.domain.repository

import com.example.pozi_android.data.remote.network.DataResult
import com.example.pozi_android.data.remote.network.PBRes

interface ServiceRepository {
    suspend fun getPBList(): DataResult<PBRes>
}