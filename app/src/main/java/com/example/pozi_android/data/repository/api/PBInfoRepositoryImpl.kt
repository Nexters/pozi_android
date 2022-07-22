package com.example.pozi_android.data.repository.api

import android.util.Log
import com.example.pozi_android.data.remote.network.DataResult
import com.example.pozi_android.data.remote.spec.PBRes
import com.example.pozi_android.domain.entity.PB
import com.example.pozi_android.domain.repository.PBInfoRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext


class PBInfoRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val firestore: FirebaseFirestore
) : PBInfoRepository {
    val t = PB(
        1,
        "경기도 성남시 분당구 삼평동 동판교로177번길 25",
        37.3972551,
        127.1134165,
        "인생네컷 판교아브뉴프랑점"
    )
    val test: List<PB> = listOf(t)
    override suspend fun getPBList(): Result = withContext(ioDispatcher) {
        return@withContext try {
            val response: QuerySnapshot = firestore.collection("인생네컷").get().await()


        }
    }

    sealed class Result {
        data class Success<T>(
            val data: T? = null
        ) : Result()

        data class Error(
            val e: Throwable
        ) : Result()

    }

}