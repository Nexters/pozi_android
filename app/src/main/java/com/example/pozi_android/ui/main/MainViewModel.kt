package com.example.pozi_android.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pozi_android.data.remote.network.Status
import com.example.pozi_android.domain.repository.PBInfoRepository
import com.example.pozi_android.domain.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val pbinfoRepository: PBInfoRepository) :
    ViewModel() {

    private val _PBListStateLiveData: MutableStateFlow<PBState> =
        MutableStateFlow(PBState.noData)
    val PBListStateLiveData: StateFlow<PBState> = _PBListStateLiveData.asStateFlow()

    fun getCenterList() {
        _PBListStateLiveData.value = PBState.Loading

        CoroutineScope(Dispatchers.IO).launch {
            val result = pbinfoRepository.getPBList()
            Log.d("임민규", result.data.toString())
            result.let {
                when (result.status) {
                    Status.SUCCESS -> {
                        if (!it.data!!.isNullOrEmpty()) {
                            _PBListStateLiveData.value = PBState.Success(it.data)
                        } else {
                            Log.d("임민규", "값이 없을때")
                        }
                    }
                    Status.ERROR -> {
                        _PBListStateLiveData.value = PBState.Error
                    }
                }

            }
        }

    }
}