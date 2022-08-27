package com.example.pozi_android.data.repository.search

import android.util.Log
import com.example.pozi_android.data.remote.network.KakaoAPI
import com.example.pozi_android.data.remote.spec.ResultSearchKeyword
import com.example.pozi_android.data.remote.url.SearchUrl
import com.example.pozi_android.domain.entity.DataResult
import com.example.pozi_android.domain.repository.KakaoSearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KakaoSearchRepositoryImpl(
    private val api: KakaoAPI,
    private val ioDispatcher: CoroutineDispatcher
) : KakaoSearchRepository {

    override suspend fun getSearchKeyword(keyword: String): DataResult<ResultSearchKeyword?> =
        withContext(ioDispatcher) {
            try {
                val response = api.getSearchKeyword(SearchUrl.KAKAO_API_KEY,keyword).execute()
                DataResult.Success(response.body())
            } catch (e: Exception) {
                DataResult.Error("서버와 연결오류")
            }
        }
}