package com.example.pozi_android.ui.searchLocation

import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.pozi_android.domain.entity.DataResult
import com.example.pozi_android.domain.mapper.PBEntityMapper
import com.example.pozi_android.domain.mapper.PlaceMapper
import com.example.pozi_android.domain.usecase.GetSearchListUseCase
import com.example.pozi_android.ui.main.CustomMarker
import com.example.pozi_android.ui.main.state.PBState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchLocationViewModel @Inject constructor(private val getSearchListUseCase: GetSearchListUseCase) :
    ViewModel() {

    private val _addressList: MutableStateFlow<List<SearchModel>> = MutableStateFlow(emptyList())
    val addressList: StateFlow<List<SearchModel>> = _addressList.asStateFlow()

    private val _noDataTextVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val noDataTextVisible: StateFlow<Boolean> = _noDataTextVisible.asStateFlow()

    fun getAddress(keyword: String) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val result = getSearchListUseCase(keyword)) {
                is DataResult.Success -> {
                    val list = result.data?.documents
                    val SearchList: List<SearchModel>
                    if (list != null) {
                        if (list.isEmpty()) {
                            _noDataTextVisible.value = true
                        } else {
                            _noDataTextVisible.value = false
                            SearchList = list.map {
                                PlaceMapper.placeToSearchModel(it)
                            }
                            _addressList.value = SearchList
                        }
                    } else {
                        Log.d("임민규다", "데이터 없음")
                    }
                }
                is DataResult.Error -> {
                    Log.d("임민규다", "에러")
                }
            }
        }
    }

    private fun getAddressWithoutCountry(address: String, country: String): String {
        return address.substring(country.length + 1)
    }
}