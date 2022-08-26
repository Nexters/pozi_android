package com.example.pozi_android.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.viewpager2.widget.ViewPager2
import com.example.pozi_android.domain.entity.Place
import com.example.pozi_android.domain.entity.DataResult
import com.example.pozi_android.domain.usecase.GetPhotoBoothListLocationUseCase
import com.example.pozi_android.ui.main.state.PBState
import com.example.pozi_android.util.PlaceUtil
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
import kotlin.math.roundToLong

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

    private val _focusedPlace = MutableLiveData<Place?>()
    val focusedPlace: LiveData<Place?> = _focusedPlace

    private val _currentLatlng: MutableLiveData<LatLng> = MutableLiveData()
    val currentLatlng: LiveData<LatLng> = _currentLatlng

    private val _currentCamera: MutableLiveData<LatLng> = MutableLiveData()
    val currentCamera: LiveData<LatLng> = _currentCamera

    private val _geocurrentLatlng: MutableLiveData<String> = MutableLiveData()
    val geocurrentLatlng: LiveData<String> = _geocurrentLatlng

    fun turnwigetVisible(visible:Boolean){
        _wigetVisibility.postValue(visible)
    }

    fun setGeoposition(position: String) {
        _geocurrentLatlng.postValue(position)
    }

    fun getZoom(zoom: Double) {
        _zoomCamera.postValue(zoom)
    }

    suspend fun outZoom() {
        placeListStateFlow.collect { uiState ->
            when (uiState) {
                is PBState.Success -> {
                    uiState.data.forEach {
                        PlaceUtil.transMarker(it)
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
                            if (it == focus) PlaceUtil.getFocus(it)
                            else PlaceUtil.loseFocus(it)
                        } else {
                            PlaceUtil.loseFocus(it)
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

    fun onPlaceClick(clickedPlace: Place): Boolean {
        if (_focusedPlace.value == clickedPlace) return true
        setFocusedPlace(clickedPlace)
        return true
    }

    fun distancetoPlace(place: Place): Long? =
        _currentLatlng.value?.distanceTo(place.marker.position)?.roundToLong()


    fun setFocusedPlace(place: Place) {
        PlaceUtil.loseFocus(_focusedPlace.value)
        PlaceUtil.getFocus(place)
        _focusedPlace.value = place
        _currentCamera.value = place.marker.position
    }

    fun getAllPlace() {
        _placeListStateFlow.value = PBState.Loading

        CoroutineScope(Dispatchers.IO).launch {
            when (val result = getPBListUseCase(_currentLatlng.value)) {
                is DataResult.Success -> {
                    _placeListStateFlow.value = PBState.Success(result.data)
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