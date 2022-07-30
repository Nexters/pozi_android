package com.example.pozi_android.data.repository.api

import android.util.Log
import com.example.pozi_android.data.remote.network.DataResult
import com.example.pozi_android.data.remote.spec.PBRes
import com.example.pozi_android.domain.entity.PB
import com.example.pozi_android.domain.mapper.PBMapper
import com.example.pozi_android.domain.repository.PBInfoRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class PBInfoRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val firestore: FirebaseFirestore
) : PBInfoRepository { //flow 써야하기 함
    val brandlist = listOf("all")
    override suspend fun getPBList(): DataResult<List<PB>> = withContext(ioDispatcher) {
        return@withContext try {
            val PBResult: MutableList<PB> = mutableListOf()
            for (brand in brandlist) {
                val response: QuerySnapshot = firestore.collection(brand).get().await()
                //Log.d("asd",response.documents.size.toString())
                PBMapper.mapperToPB(PBResult, response.documents.map {
                    PBRes(
                        address = it.get("address") as String,
                        coordinates = it.get("coordinates") as Map<String, Double>,
                        phoneNumber = it.get("phoneNumber") as String,
                        subject = it.get("subject") as String
                    )
                }, PBResult.size, brand)
            }

            DataResult.success(PBResult.toList())

        } catch (e: Exception) {
            DataResult.error(null, "서버와 연결오류")
        }
    }
}