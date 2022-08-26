package com.example.pozi_android.data.repository.firebase

import android.util.Log
import com.example.pozi_android.domain.entity.DataResult
import com.example.pozi_android.domain.entity.Place
import com.example.pozi_android.domain.mapper.PlaceMapper
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

    override suspend fun getPBLocation(latLng: LatLng): DataResult<List<Place>> =
        withContext(ioDispatcher) {
            return@withContext try {
                val response: QuerySnapshot = firestore.collection("all").get().await()
                Log.d("민규", latLng.toString())
                var id: Long = 0
                val placeList = mutableListOf<Place>()
                response.documents.map {
                    var coord: Map<String, Double> = it.get("coordinates") as Map<String, Double>
                    val test = LatLng(coord["_latitude"] as Double, coord["_latitude"] as Double)
                    val distance = test.distanceTo(latLng)
                    Log.d("민규", it.get("subject") as String + distance)
                    //if (distance <= 500000) {
                        placeList.add(PlaceMapper.mapperToPlace(id++, it))
                    //}
                }

                DataResult.Success(placeList.toList())

            } catch (e: Exception) {
                Log.e("우라라", e.toString())
                DataResult.Error("서버와 연결오류")
            }
        }

    companion object {
        const val REFERANCE_LAT = 20 / 109.958489129649955
        const val REFERANCE_LNG = 20 / 88.74
    }
}