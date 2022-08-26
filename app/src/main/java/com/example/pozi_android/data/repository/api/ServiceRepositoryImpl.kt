//package com.example.pozi_android.data.repository.api
//
//import com.example.pozi_android.data.remote.network.RetrofitInterface
//import com.example.pozi_android.domain.entity.DataResult
//import com.example.pozi_android.domain.entity.Place
//import com.example.pozi_android.domain.mapper.PlaceMapper
//import com.example.pozi_android.domain.repository.ServiceRepository
//import kotlinx.coroutines.CoroutineDispatcher
//import kotlinx.coroutines.withContext
//
//class ServiceRepositoryImpl(
//    private val api: RetrofitInterface,
//    private val ioDispatcher: CoroutineDispatcher
//) : ServiceRepository {
//
//    override suspend fun getPBList(): DataResult<List<Place>> = withContext(ioDispatcher) {
//        try {
//            val response = api.getPhotoBoothList()
//            when {
//                response.isSuccessful -> {
//                    var id: Long = 0
//                    var placeList = mutableListOf<Place>()
//
//                    response.body()!!.result.forEach {
//                        placeList.add(PlaceMapper.mapperToPlace(id++, it))
//                    }
//                    DataResult.Success(placeList)
//                }
//                else -> {
//                    DataResult.Error("데이터를 못받아옴")
//                }
//            }
//        } catch (e: Exception) {
//            DataResult.Error("서버와 연결오류")
//        }
//    }
//
//}