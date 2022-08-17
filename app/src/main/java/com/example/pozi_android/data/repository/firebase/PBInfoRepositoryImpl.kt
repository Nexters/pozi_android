package com.example.pozi_android.data.repository.firebase

import android.util.Log
import com.example.pozi_android.domain.entity.DataResult
import com.example.pozi_android.domain.entity.Place
import com.example.pozi_android.domain.mapper.PlaceMapper
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

    override suspend fun getPBList(): DataResult<List<Place>> = withContext(ioDispatcher) {
        return@withContext try {
            val response: QuerySnapshot = firestore.collection("all").get().await()

            var id: Long = 0
            val placeList: List<Place> = response.documents.map {
                PlaceMapper.mapperToPlace(id++, it)
            }

            DataResult.Success(placeList)

        } catch (e: Exception) {
            Log.e("우라라", e.toString())
            DataResult.Error("서버와 연결오류")
        }
    }
}