package com.example.pozi_android.ui.main

import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pozi_android.domain.entity.DataResult
import com.example.pozi_android.domain.mapper.PBEntityMapper
import com.example.pozi_android.domain.usecase.GetPhotoBoothListLocationUseCase
import com.example.pozi_android.ui.main.state.PBState
import com.example.pozi_android.util.CustomMarkerUtil
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPBListUseCase: GetPhotoBoothListLocationUseCase,
) : ViewModel() {

    private val _placeListStateFlow: MutableStateFlow<PBState> = MutableStateFlow(PBState.NoData)
    val placeListStateFlow: StateFlow<PBState> = _placeListStateFlow.asStateFlow()

    private val _wigetVisibility: MutableLiveData<Boolean> = MutableLiveData()
    val wigetVisibility: LiveData<Boolean> = _wigetVisibility

    private val _zoomCamera: MutableLiveData<Double> = MutableLiveData()
    val zoomCamera: LiveData<Double> = _zoomCamera

    private val _focusedPlace = MutableLiveData<CustomMarker?>()
    val focusedCustomMarker: LiveData<CustomMarker?> = _focusedPlace

    private val _currentLatlng: MutableLiveData<LatLng> = MutableLiveData()
    val currentLatlng: LiveData<LatLng> = _currentLatlng

    private val _currentCamera: MutableLiveData<LatLng> = MutableLiveData()
    val currentCamera: LiveData<LatLng> = _currentCamera

    private val _geocurrentLatlng: MutableLiveData<String> = MutableLiveData()
    val geocurrentLatlng: LiveData<String> = _geocurrentLatlng

    fun turnwigetVisible(visible: Boolean) {
        _wigetVisibility.postValue(visible)
    }

    fun setGeoposition(position: String) {
        _geocurrentLatlng.postValue(position)
    }

    fun setZoom(zoom: Double) {
        _zoomCamera.postValue(zoom)
    }

    suspend fun outZoom() {
        placeListStateFlow.collect { uiState ->
            when (uiState) {
                is PBState.Success -> {
                    uiState.data.forEach {
                        CustomMarkerUtil.transMarker(it)
                    }
                }
            }
        }
    }

    suspend fun inZoom() {
        placeListStateFlow.collect { uiState ->
            when (uiState) {
                is PBState.Success -> {
                    uiState.data.forEach {
                        if (_focusedPlace.value != null) {
                            val focus = _focusedPlace.value
                            if (it == focus) CustomMarkerUtil.getFocus(it)
                            else CustomMarkerUtil.loseFocus(it)
                        } else {
                            CustomMarkerUtil.loseFocus(it)
                        }
                    }
                }
            }
        }
    }

    fun currentPositionListener(latLng: LatLng) {
        _currentLatlng.postValue(LatLng(latLng.latitude, latLng.longitude))
    }

    fun currentCamera(latLng: LatLng) {
        _currentCamera.postValue(LatLng(latLng.latitude, latLng.longitude))
    }

    fun onPlaceClick(clickedCustomMarker: CustomMarker): Boolean {
        if (_focusedPlace.value == clickedCustomMarker) return true
        setFocusedPlace(clickedCustomMarker)
        return true
    }

    fun setFocusedPlace(customMarker: CustomMarker) {
        CustomMarkerUtil.loseFocus(_focusedPlace.value)
        CustomMarkerUtil.getFocus(customMarker)
        _focusedPlace.value = customMarker
        _currentCamera.value = customMarker.marker.position
        setZoom(16.0)
        CoroutineScope(Dispatchers.IO).launch {
            inZoom()
        }
    }

    fun getAddress(geocoder: Geocoder, latLng: LatLng) {
        val address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).firstOrNull()
        val fullAddress = address?.getAddressLine(0).toString()
        val contry = address?.countryName ?: "null"
        if (contry != "대한민국") {
            _geocurrentLatlng.value = "주소를 가져 올 수 없습니다."
            return
        }
        val countryLength = address?.countryName?.length ?: -1
        val result = fullAddress.substring(countryLength + 1)

        _geocurrentLatlng.value = result
    }

    fun getPBListChangeAdress() {
        _placeListStateFlow.value = PBState.Loading

        CoroutineScope(Dispatchers.IO).launch {
            when (val result = getPBListUseCase(_currentLatlng.value)) {
                is DataResult.Success -> {
                    Log.d("임민규",result.data.toString())
                    var id: Long = 0
                    val placelist: List<CustomMarker> = result.data.map {
                        PBEntityMapper.PBEntityToCustomMarker(id++, it)
                    }
                    _placeListStateFlow.value = PBState.Success(placelist)
                }
                is DataResult.NoData -> {
                    _placeListStateFlow.value = PBState.NoData
                }
                is DataResult.Error -> {
                    _placeListStateFlow.value = PBState.Error
                }
            }
            _focusedPlace.postValue(null)
        }

    }

}