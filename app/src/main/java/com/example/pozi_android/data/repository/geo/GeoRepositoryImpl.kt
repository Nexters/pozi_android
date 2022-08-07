package com.example.pozi_android.data.repository.geo

import android.util.Log
import com.example.pozi_android.data.remote.network.DataResult
import com.example.pozi_android.data.remote.spec.address.GeoResponse
import com.example.pozi_android.data.remote.spec.address.GeoService
import com.example.pozi_android.domain.repository.geo.GeoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GeoRepositoryImpl(
    private val api: GeoService,
    private val ioDispatcher: CoroutineDispatcher
) : GeoRepository {
    override suspend fun test(x: Double, y: Double) {
        Log.d("민규민규123", x.toString())
        Log.d("민규민규123", y.toString())
        Log.d("민규민규123", api.toString())
    }

    override suspend fun getAdressInfo(
        x: Double,
        y: Double,
    ): DataResult<GeoResponse> = withContext(ioDispatcher) {
        return@withContext try {
            val response = api.getAddress("$x,$y")
            when {
                response.isSuccessful -> {
                    DataResult.success(response.body()!!)
                }
                else -> {
                    Log.d("민규민규", "오류1")
                    DataResult.error(null, "오류")
                }
            }
        } catch (e: Exception) {
            Log.d("민규민규", "오류2")
            Log.e("민규민규","오류2",e)
            DataResult.error(null, "서버와 연결오류")
        }
    }
}