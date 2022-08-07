package com.example.pozi_android.domain.repository.geo

import com.example.pozi_android.data.remote.network.DataResult
import com.example.pozi_android.data.remote.spec.address.GeoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface GeoRepository {
    suspend fun getAdressInfo(
        x: Double, y: Double
    ): DataResult<GeoResponse>

    suspend fun test(x: Double, y: Double)
}