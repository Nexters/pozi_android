package com.example.pozi_android.ui.main

import androidx.lifecycle.ViewModel
import com.example.pozi_android.domain.entity.DataResult
import com.example.pozi_android.domain.usecase.GetPhotoBoothListUseCase
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

    fun getCenterList() {
        _PBListStateFlow.value = PBState.Loading

        CoroutineScope(Dispatchers.IO).launch {
            when (val result = getPBListUseCase()) {
                is DataResult.Success -> {
                    _PBListStateFlow.value = PBState.Success(result.data)
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
}