package com.example.pozi_android.data.repository.api

import com.example.pozi_android.data.remote.network.DataResult
import com.example.pozi_android.data.remote.network.PBRes
import com.example.pozi_android.data.remote.network.RetrofitInterface
import com.example.pozi_android.domain.repository.ServiceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ServiceRepositoryImpl(
    private val api: RetrofitInterface,
    private val ioDispatcher: CoroutineDispatcher
) : ServiceRepository {

    override suspend fun getPBList(): DataResult<PBRes> = withContext(ioDispatcher) {
        try {
            val response = api.getPhotoBoothList()
            when {
                response.isSuccessful -> {
                    DataResult.success(response.body()!!)
                }
                else -> {
                    DataResult.error(null, "오류")
                }
            }
        } catch (e: Exception) {
            DataResult.error(null, "서버와 연결오류")
        }
    }

}