package com.example.pozi_android.domain.repository

import com.example.pozi_android.data.remote.spec.ResultSearchKeyword
import com.example.pozi_android.domain.entity.DataResult

interface KakaoSearchRepository {

    suspend fun getSearchKeyword(keyword: String): DataResult<ResultSearchKeyword?>

}