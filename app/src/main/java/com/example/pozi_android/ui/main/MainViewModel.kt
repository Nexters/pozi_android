package com.example.pozi_android.ui.main

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import com.example.pozi_android.data.remote.network.Status
import com.example.pozi_android.domain.repository.PBInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class MainViewModel @Inject constructor(
    private val pbinfoRepository: PBInfoRepository
) :
    ViewModel() {
    private val _PBListStateFlow: MutableStateFlow<PBState> =
        MutableStateFlow(PBState.noData)
    val PBListStateFlow: StateFlow<PBState> = _PBListStateFlow.asStateFlow()

    fun getCenterList() {
        _PBListStateFlow.value = PBState.Loading

        CoroutineScope(Dispatchers.IO).launch {
            val result = pbinfoRepository.getPBList()
            Log.d("임민규", result.data.toString())
            result.let {
                when (result.status) {
                    Status.SUCCESS -> {
                        if (!it.data!!.isNullOrEmpty()) {
                            _PBListStateFlow.value = PBState.Success(it.data)
                        } else {
                            Log.d("임민규", "값이 없을때")
                        }
                    }
                    Status.ERROR -> {
                        _PBListStateFlow.value = PBState.Error
                    }
                }

            }
        }

    }

}