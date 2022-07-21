package com.example.pozi_android.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pozi_android.data.remote.network.DataResult
import com.example.pozi_android.data.remote.network.PBListRes
import com.example.pozi_android.data.remote.network.Status
import com.example.pozi_android.domain.entity.PB
import com.example.pozi_android.domain.repository.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val serviceRepository: ServiceRepository) :
    ViewModel() {

    private val _PBListStateLiveData: MutableLiveData<PBState> =
        MutableLiveData()
    val PBListStateLiveData: LiveData<PBState> get() = _PBListStateLiveData

    fun getCenterList() {
        _PBListStateLiveData.value = PBState.Loading

        CoroutineScope(Dispatchers.IO).launch {
            val result = serviceRepository.getPBList()
            Log.d("임민규", result.data.toString())
            result.let {
                when (result.status) {
                    Status.SUCCESS -> {
                        if (!it.data!!.isNullOrEmpty()) {
                            _PBListStateLiveData.postValue(PBState.Success(it.data))
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