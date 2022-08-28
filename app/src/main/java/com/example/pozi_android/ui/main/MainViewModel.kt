package com.example.pozi_android.ui.main

import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.pozi_android.domain.entity.DataResult
import com.example.pozi_android.domain.mapper.PBEntityMapper
import com.example.pozi_android.domain.usecase.GetPhotoBoothListLocationUseCase
import com.example.pozi_android.ui.main.model.CustomMarkerModel
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
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPBListUseCase: GetPhotoBoothListLocationUseCase,
) : ViewModel() {

    private val _placeListStateFlow: MutableStateFlow<PBState> = MutableStateFlow(PBState.Init)
    val placeListStateFlow: StateFlow<PBState> = _placeListStateFlow.asStateFlow()

    private val _wigetVisibilityStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val wigetVisibilityStateFlow: StateFlow<Boolean> = _wigetVisibilityStateFlow.asStateFlow()

    private val _zoomCamera: MutableStateFlow<Double> = MutableStateFlow(16.0)
    val zoomCamera: StateFlow<Double> = _zoomCamera.asStateFlow()

    private val _focusedCustomMarkerModelStateFlow: MutableStateFlow<CustomMarkerModel?> =
        MutableStateFlow(null)
    val focusedCustomMarkerModelStateFlow: StateFlow<CustomMarkerModel?> = _focusedCustomMarkerModelStateFlow

    private val _currentLatlngStateFlow: MutableStateFlow<LatLng> =
        MutableStateFlow(LatLng.INVALID)
    val currentLatlngStateFlow: StateFlow<LatLng> = _currentLatlngStateFlow.asStateFlow()

    private val _geocurrentLatlngStateFlow: MutableStateFlow<String> =
        MutableStateFlow(GangnamAddress)
    val geocurrentLatlngStateFlow: StateFlow<String> = _geocurrentLatlngStateFlow.asStateFlow()

    private val _currentCameraStateFlow: MutableStateFlow<LatLng> =
        MutableStateFlow(LatLng(GangnamLat, GangnamLng))
    val currentCameraStateFlow: StateFlow<LatLng> = _currentCameraStateFlow.asStateFlow()

    fun turnwigetVisible(visible: Boolean) {
        _wigetVisibilityStateFlow.value = visible
    }

    fun setZoom(zoom: Double) {
        _zoomCamera.value = zoom
    }

    fun outZoom() {
        CoroutineScope(Dispatchers.IO).launch {
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
    }

    fun inZoom() {
        CoroutineScope(Dispatchers.IO).launch {
            placeListStateFlow.collect { uiState ->
                when (uiState) {
                    is PBState.Success -> {
                        uiState.data.forEach {
                            if (focusedCustomMarkerModelStateFlow.value != null) {
                                val focus = focusedCustomMarkerModelStateFlow.value
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
    }

    fun currentPosition(latLng: LatLng) {
        Log.d("민규입니다다","currentPosition")
        _currentLatlngStateFlow.value = latLng
    }

    fun currentCamera(latLng: LatLng) {
        _currentCameraStateFlow.value = latLng
    }

    fun onPlaceClickListener(customMarkerModel: CustomMarkerModel): Boolean {
        if (focusedCustomMarkerModelStateFlow.value == customMarkerModel) return true
        setFocusedPlace(customMarkerModel)
        return true
    }

    fun setFocusedCustomMarker(customMarkerModel: CustomMarkerModel?) {
        _focusedCustomMarkerModelStateFlow.value = customMarkerModel
    }

    fun setFocusedPlace(customMarkerModel: CustomMarkerModel) {
        CustomMarkerUtil.loseFocus(focusedCustomMarkerModelStateFlow.value)
        CustomMarkerUtil.getFocus(customMarkerModel)
        setFocusedCustomMarker(customMarkerModel)
        currentCamera(customMarkerModel.marker.position)
        setZoom(16.0)
        inZoom()
    }

    fun setGeoCurrentLatlng(address: String) {
        _geocurrentLatlngStateFlow.value = address
    }

    fun getAddress(geocoder: Geocoder, latLng: LatLng) {
        val address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).firstOrNull()
        val fullAddress = address?.getAddressLine(0).toString()
        val contry = address?.countryName ?: ""
        if (contry != "대한민국") {
            setGeoCurrentLatlng("주소를 가져 올 수 없습니다.")
            return
        }
        val countryLength = address?.countryName?.length ?: -1
        val result = fullAddress.substring(countryLength + 1)

        setGeoCurrentLatlng(result)
    }

    fun getPBListChangeAdress() {
        _placeListStateFlow.value = PBState.Loading

        if(currentLatlngStateFlow.value == LatLng.INVALID) return

        CoroutineScope(Dispatchers.IO).launch {
            when (val result = getPBListUseCase(currentLatlngStateFlow.value)) {
                is DataResult.Success -> {
                    var id: Long = 0
                    val placelist: List<CustomMarkerModel> = result.data.map {
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
            setFocusedCustomMarker(null)
        }

    }

    companion object {
        private const val GangnamLat = 37.497885
        private const val GangnamLng = 127.027512
        private const val GangnamAddress = "서울특별시 강남구 역삼동 858-50"
    }

}