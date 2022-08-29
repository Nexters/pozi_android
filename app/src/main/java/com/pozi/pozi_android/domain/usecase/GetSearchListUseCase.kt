package com.pozi.pozi_android.domain.usecase

import com.pozi.pozi_android.data.remote.spec.ResultSearchKeyword
import com.pozi.pozi_android.domain.entity.DataResult
import com.pozi.pozi_android.domain.repository.KakaoSearchRepository
import javax.inject.Inject

class GetSearchListUseCase @Inject constructor(
    private val searchRepository: KakaoSearchRepository
) {
    suspend operator fun invoke(keyword: String): DataResult<ResultSearchKeyword> {
        return searchRepository.getSearchKeyword(keyword)
    }
}