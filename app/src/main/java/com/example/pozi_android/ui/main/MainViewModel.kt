package com.example.pozi_android.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pozi_android.domain.entity.CustomMarker
import com.example.pozi_android.domain.entity.DataResult
import com.example.pozi_android.domain.entity.PBEntity
import com.example.pozi_android.domain.mapper.MarkerMapper
import com.example.pozi_android.domain.usecase.GetPhotoBoothListUseCase
import com.example.pozi_android.ui.main.state.MarkerState
import com.example.pozi_android.ui.main.state.PBState
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPBListUseCase: GetPhotoBoothListUseCase,
) : ViewModel() {

    private val _PBListStateFlow: MutableStateFlow<PBState> = MutableStateFlow(PBState.NoData)
    val PBListStateFlow: StateFlow<PBState> = _PBListStateFlow.asStateFlow()

    val _wigetVisibility: MutableLiveData<Boolean> = MutableLiveData()
    val wigetVisibility: LiveData<Boolean> = _wigetVisibility

    private val _moveCamera: MutableLiveData<LatLng> = MutableLiveData()
    val moveCamera: LiveData<LatLng> = _moveCamera

    private val _markerList: MutableLiveData<List<CustomMarker>> = MutableLiveData()
    val markerList: LiveData<List<CustomMarker>> = _markerList

    private val _markerstate: MutableStateFlow<MarkerState> = MutableStateFlow(MarkerState.NoData)
    val markerstate: StateFlow<MarkerState> = _markerstate


    fun getCenterList() {
        _PBListStateFlow.value = PBState.Loading

        CoroutineScope(Dispatchers.IO).launch {
            when (val result = getPBListUseCase()) {
                is DataResult.Success -> {
                    _PBListStateFlow.value = PBState.Success(result.data)
                    attachMarkerList(result.data)
                }
                is DataResult.NoData -> {
                    _PBListStateFlow.value = PBState.NoData
                }
                is DataResult.Error -> {
                    _PBListStateFlow.value = PBState.Error
                }
            }
        }
    }

    fun attachMarkerList(list: List<PBEntity>) {
        val markerlist: List<CustomMarker> = list.map {
            MarkerMapper.entityToCustomMarker(it)
        }
        _markerList.postValue(markerlist)
    }

    fun onoffMarker(customMarker: CustomMarker) {
        if(customMarker == null){
            val prev = (markerstate.value as MarkerState.On).prev
            _markerstate.value = MarkerState.Off(prev)
        }
        when (markerstate.value) {
            is MarkerState.NoData -> {
                _markerstate.value = MarkerState.On(customMarker)
            }
            is MarkerState.On -> {
                val prev = (markerstate.value as MarkerState.On).prev
                _markerstate.value = MarkerState.Off(prev)
                _markerstate.value = MarkerState.On(customMarker)
            }
        }
    }

    fun setMapClickListener(naverMap: NaverMap) =
        naverMap.setOnMapClickListener { point, coord ->
            _wigetVisibility.value = false
        }

    fun markerClickListener(latLng: LatLng) {
        _moveCamera.value = latLng
        _wigetVisibility.value = true
    }

}