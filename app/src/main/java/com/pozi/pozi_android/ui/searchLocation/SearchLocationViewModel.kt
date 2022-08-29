package com.pozi.pozi_android.ui.searchLocation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.pozi.pozi_android.domain.entity.DataResult
import com.pozi.pozi_android.domain.mapper.PlaceMapper
import com.pozi.pozi_android.domain.usecase.GetSearchListUseCase
import com.pozi.pozi_android.ui.searchLocation.model.SearchModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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
                    turnnoDataText(false)
                    val searchList = result.data.documents.map {
                        PlaceMapper.placeToSearchModel(it)
                    }
                    _addressList.value = searchList
                }
                is DataResult.NoData -> {
                    turnnoDataText(true)
                }
                is DataResult.Error -> {
                    Log.d("임민규다", "에러")
                }
            }
        }
    }

    private fun turnnoDataText(turn: Boolean) {
        _noDataTextVisible.value = turn
    }
}