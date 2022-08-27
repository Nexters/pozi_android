package com.example.pozi_android.ui.main.state

import com.example.pozi_android.ui.main.Place

sealed class PBState {
    object Loading : PBState()
    object NoData : PBState()
    data class Success(val data: List<Place>) : PBState()
    object Error : PBState()
}
