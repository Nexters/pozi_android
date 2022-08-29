package com.pozi.pozi_android.data.repository.search

import com.pozi.pozi_android.data.remote.network.KakaoAPI
import com.pozi.pozi_android.data.remote.spec.ResultSearchKeyword
import com.pozi.pozi_android.data.remote.url.SearchUrl
import com.pozi.pozi_android.domain.entity.DataResult
import com.pozi.pozi_android.domain.repository.KakaoSearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class KakaoSearchRepositoryImpl(
    private val api: KakaoAPI,
    private val ioDispatcher: CoroutineDispatcher
) : KakaoSearchRepository {

    override suspend fun getSearchKeyword(keyword: String): DataResult<ResultSearchKeyword> =
        withContext(ioDispatcher) {
            try {
                val response = api.getSearchKeyword(SearchUrl.KAKAO_API_KEY, keyword).execute()
                val result: ResultSearchKeyword? = response.body()
                if (result == null) {
                    DataResult.Error("search : null")
                } else {
                    if (result.documents.isEmpty()) {
                        DataResult.NoData
                    } else {
                        DataResult.Success(result)
                    }
                }
            } catch (e: Exception) {
                DataResult.Error("서버와 연결오류")
            }
        }
}