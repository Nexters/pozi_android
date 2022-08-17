package com.example.pozi_android.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.viewpager2.widget.ViewPager2
import com.example.pozi_android.domain.entity.Place
import com.example.pozi_android.domain.entity.DataResult
import com.example.pozi_android.domain.usecase.GetPhotoBoothListUseCase
import com.example.pozi_android.ui.main.state.PBState
import com.example.pozi_android.util.PlaceUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapView
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

    private val _placeListStateFlow: MutableStateFlow<PBState> = MutableStateFlow(PBState.NoData)
    val placeListStateFlow: StateFlow<PBState> = _placeListStateFlow.asStateFlow()

    val _wigetVisibility: MutableLiveData<Boolean> = MutableLiveData() //private 해줘도 되는지 확인 하기
    val wigetVisibility: LiveData<Boolean> = _wigetVisibility

    private val _moveCamera: MutableLiveData<LatLng> = MutableLiveData()
    val moveCamera: LiveData<LatLng> = _moveCamera

    private val _focusedPlace = MutableLiveData<Place?>()
    val focusedPlace: LiveData<Place?> = _focusedPlace

    private val _addressText = MutableLiveData("")
    val addressText: LiveData<String> = _addressText

    fun onPlaceClick(clickedPlace: Place): Boolean {
        if (_focusedPlace.value == clickedPlace) return true
        setFocusedPlace(clickedPlace)
        return true
    }

    fun setFocusedPlace(place: Place) {
        PlaceUtil.loseFocus(_focusedPlace.value)
        PlaceUtil.getFocus(place)
        _focusedPlace.value = place
        _moveCamera.value = place.marker.position
    }

    fun moveCam(latitude: Double, longitude: Double) {
        _moveCamera.value = LatLng(latitude, longitude)
    }

    fun setMapClickListener(naverMap: NaverMap) =
        naverMap.setOnMapClickListener { _, coord ->
            PlaceUtil.loseFocus(_focusedPlace.value)
        }

    fun getAllPlace() {
        _placeListStateFlow.value = PBState.Loading

        CoroutineScope(Dispatchers.IO).launch {
            when (val result = getPBListUseCase()) {
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

    fun markertoWiget(
        placa: Place,
        viewPager: ViewPager2,
        viewPagerAdapter: MainPBInfoPagerAdapter
    ) {
        _wigetVisibility.value = true
        val selectedModel = viewPagerAdapter.currentList.firstOrNull {
            it.id == placa.id
        }
        selectedModel?.let {
            val position = viewPagerAdapter.currentList.indexOf(it)
            viewPager.currentItem = position
        }
    }

    fun clicklistnerMarker(
        place: Place,
        viewPager: ViewPager2,
        viewPagerAdapter: MainPBInfoPagerAdapter
    ) {
        place.marker.setOnClickListener {
            onPlaceClick(place)
            markertoWiget(place, viewPager, viewPagerAdapter)
            true
        }
    }

    fun attachMarker(
        list: List<Place>, mapView: MapView, viewPager: ViewPager2,
        viewPagerAdapter: MainPBInfoPagerAdapter
    ) {
        mapView.getMapAsync { naverMap ->
            list.forEach {
                clicklistnerMarker(it, viewPager, viewPagerAdapter)
                it.marker.map = naverMap
            }
        }
    }

}