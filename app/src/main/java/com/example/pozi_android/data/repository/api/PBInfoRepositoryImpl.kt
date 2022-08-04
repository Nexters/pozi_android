package com.example.pozi_android.data.repository.api

import com.example.pozi_android.domain.entity.DataResult
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
) : PBInfoRepository {

    override suspend fun getPBList(): DataResult<List<PB>> = withContext(ioDispatcher) {
        return@withContext try {
            val response: QuerySnapshot = firestore.collection("all").get().await()

            var id = 0
            val PBList: List<PB> = response.documents.map {
                PBMapper.mapToEntity(id++, it)
            }

            DataResult.Success(PBList)

        } catch (e: Exception) {
            DataResult.Error("서버와 연결오류")
        }
    }
}