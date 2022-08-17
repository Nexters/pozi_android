package com.example.pozi_android.ui.searchLocation

import android.location.Geocoder
import androidx.lifecycle.ViewModel
import com.example.pozi_android.domain.entity.DataResult
import com.example.pozi_android.domain.usecase.GetPhotoBoothListUseCase
import com.example.pozi_android.ui.main.state.PBState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SearchLocationViewModel @Inject constructor() : ViewModel() {

    private val _addressList: MutableStateFlow<List<LocationModel>> = MutableStateFlow(emptyList())
    val addressList: StateFlow<List<LocationModel>> = _addressList.asStateFlow()

    fun getAddress(geocoder: Geocoder, keyword: String) {
        try {
            geocoder.getFromLocationName(keyword, 10)
                .mapNotNull { address ->
                    address?.let {
                        LocationModel(
                            title = it.featureName ?: "",
                            subTitle = getAddressWithoutCountry(
                                it.getAddressLine(0),
                                it.countryName
                            ),
                            latitude = it.latitude,
                            longitude = it.longitude
                        )
                    }
                }.also {
                    _addressList.value = it
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getAddressWithoutCountry(address: String, country: String): String {
        return address.substring(country.length + 1)
    }
}