package com.example.pozi_android.data.repository.firebase

import android.util.Log
import com.example.pozi_android.domain.entity.DataResult
import com.example.pozi_android.domain.entity.PBEntity
import com.example.pozi_android.domain.mapper.PBEntityMapper
import com.example.pozi_android.domain.repository.PBInfoRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class PBInfoRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val firestore: FirebaseFirestore
) : PBInfoRepository {

    override suspend fun getPBLocation(latLng: LatLng): DataResult<List<PBEntity>> =
        withContext(ioDispatcher) {
            return@withContext try {
                val response: QuerySnapshot = firestore.collection("all").get().await()
                var placeList = mutableListOf<PBEntity>()
                response.documents.map {
                    var coord: Map<String, Double> = it.get("coordinates") as Map<String, Double>
                    val data_latlng = LatLng(coord["_latitude"] as Double, coord["_longitude"] as Double)
                    val dis = data_latlng.distanceTo(latLng)
                    if (dis <= 5000) {
                        placeList.add(PBEntityMapper.mapperToPBEntity(dis,it))
                    }
                }
                Log.d("임민규",placeList.toString())
                placeList= placeList.sortedBy { it.distance } as MutableList<PBEntity>
                Log.d("임민규",placeList.toString())
                DataResult.Success(placeList.toList())

            } catch (e: Exception) {
                Log.e("우라라", e.toString())
                DataResult.Error("서버와 연결오류")
            }
        }
}