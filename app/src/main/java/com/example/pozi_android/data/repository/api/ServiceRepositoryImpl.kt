package com.example.pozi_android.data.repository.api

import android.util.Log
import com.example.pozi_android.data.remote.network.RetrofitInterface
import com.example.pozi_android.domain.entity.DataResult
import com.example.pozi_android.domain.entity.PBEntity
import com.example.pozi_android.domain.mapper.PBMapper
import com.example.pozi_android.domain.repository.ServiceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ServiceRepositoryImpl(
    private val api: RetrofitInterface,
    private val ioDispatcher: CoroutineDispatcher
) : ServiceRepository {

    override suspend fun getPBList(): DataResult<List<PBEntity>> = withContext(ioDispatcher) {
        try {
            val response = api.getPhotoBoothList()
            when {
                response.isSuccessful -> {
                    var id = 0
                    var PBEntityList = mutableListOf<PBEntity>()

                    response.body()!!.result.forEach {
                        PBEntityList.add(PBMapper.mapperToPB(id++, it))
                    }
                    DataResult.Success(PBEntityList)
                }
                else -> {
                    DataResult.Error("데이터를 못받아옴")
                }
            }
        } catch (e: Exception) {
            DataResult.Error("서버와 연결오류")
        }
    }

}