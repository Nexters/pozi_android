package com.pozi.pozi_android.domain.repository

import com.pozi.pozi_android.data.remote.spec.ResultSearchKeyword
import com.pozi.pozi_android.domain.entity.DataResult

interface KakaoSearchRepository {
    suspend fun getSearchKeyword(keyword: String): DataResult<ResultSearchKeyword>
}