package com.pozi.pozi_android.ui.main.state

import com.pozi.pozi_android.ui.main.model.CustomMarkerModel

sealed class PBState {
    object Init : PBState()
    object Loading : PBState()
    object NoData : PBState()
    data class Success(val data: List<CustomMarkerModel>) : PBState()
    object Error : PBState()
}
