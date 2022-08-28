package com.example.pozi_android.domain.usecase

import com.example.pozi_android.data.remote.spec.ResultSearchKeyword
import com.example.pozi_android.domain.entity.DataResult
import com.example.pozi_android.domain.entity.PBEntity
import com.example.pozi_android.domain.repository.KakaoSearchRepository
import com.example.pozi_android.domain.repository.PBInfoRepository
import com.naver.maps.geometry.LatLng
import javax.inject.Inject

class GetSearchListUseCase @Inject constructor(
    private val searchRepository: KakaoSearchRepository
) {
    suspend operator fun invoke(keyword: String): DataResult<ResultSearchKeyword> {
        return searchRepository.getSearchKeyword(keyword)
    }
}