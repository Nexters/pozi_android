package com.example.pozi_android.ui.main

import com.example.pozi_android.domain.entity.PB

sealed class PBState {
    object Loading : PBState()
    object NoData : PBState()
    data class Success(val data: List<PB>) : PBState()
    object Error : PBState()
}
