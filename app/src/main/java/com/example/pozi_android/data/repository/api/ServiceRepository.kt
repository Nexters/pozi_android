package com.example.pozi_android.data.repository.api

import com.example.pozi_android.data.remote.network.DataResult
import com.example.pozi_android.data.remote.network.LocationRes

interface ServiceRepository {
    suspend fun getPhotoBoothList(): DataResult<LocationRes>
}