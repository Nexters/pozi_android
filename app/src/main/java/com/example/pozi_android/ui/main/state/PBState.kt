package com.example.pozi_android.ui.main.state

import com.example.pozi_android.ui.main.CustomMarker

sealed class PBState {
    object Loading : PBState()
    object NoData : PBState()
    data class Success(val data: List<CustomMarker>) : PBState()
    object Error : PBState()
}
