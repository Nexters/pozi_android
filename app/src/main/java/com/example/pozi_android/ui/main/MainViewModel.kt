package com.example.pozi_android.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pozi_android.domain.entity.Place
import com.example.pozi_android.domain.entity.DataResult
import com.example.pozi_android.domain.entity.PBEntity
import com.example.pozi_android.domain.mapper.MarkerMapper
import com.example.pozi_android.domain.usecase.GetPhotoBoothListUseCase
import com.example.pozi_android.ui.main.state.PBState
import com.example.pozi_android.util.PlaceUtil
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

    private val _markerList: MutableLiveData<List<Place>> = MutableLiveData()
    val markerList: LiveData<List<Place>> = _markerList

    private val _focusedPlace = MutableLiveData<Place?>()
    val focusedPlace: LiveData<Place?> = _focusedPlace

    private val _addressText = MutableLiveData("")
    val addressText: LiveData<String> = _addressText

    fun onPlaceClick(clickedPlace: Place): Boolean {
        Log.d("다은",clickedPlace.brandName)
        // 기존 focused place 클릭 -> do nothing
        if (_focusedPlace.value == clickedPlace) return true
        Log.d("다은",clickedPlace.brandName + "1")
        setFocusedPlace(clickedPlace)

        return true
    }

    private fun setFocusedPlace(place: Place) {
        PlaceUtil.loseFocus(_focusedPlace.value)
        PlaceUtil.getFocus(place)
        Log.d("다은",_focusedPlace.value?.brandName + "2")
        Log.d("다은",place.brandName + "3")
        _focusedPlace.value = place
    }

    fun setMapClickListener(naverMap: NaverMap) =
        naverMap.setOnMapClickListener { _, coord ->
            PlaceUtil.loseFocus(_focusedPlace.value)
        }

    fun placesItemClick(place: Place) {
        setFocusedPlace(place)
    }

    fun getAllPlace() {
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

            _focusedPlace.postValue(null)
        }
    }

    fun attachMarkerList(list: List<PBEntity>) {
        val markerlist: List<Place> = list.map {
            MarkerMapper.entityToCustomMarker(it)

        }
        _markerList.postValue(markerlist)
    }

    fun markerClickListener(latLng: LatLng) {
        _moveCamera.value = latLng
        _wigetVisibility.value = true
    }
}