package com.example.pozi_android.ui.main

import com.example.pozi_android.domain.entity.PBEntity

sealed class PBState {
    object Loading : PBState()
    object noData : PBState()
    data class Success(val data: List<PBEntity>) : PBState()
    object Error : PBState()
}
