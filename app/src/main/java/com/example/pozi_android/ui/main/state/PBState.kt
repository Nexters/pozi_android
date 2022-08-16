package com.example.pozi_android.ui.main.state

import com.example.pozi_android.domain.entity.PBEntity

sealed class PBState {
    object Loading : PBState()
    object NoData : PBState()
    data class Success(val data: List<PBEntity>) : PBState()
    object Error : PBState()
}
