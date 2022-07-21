package com.example.pozi_android.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pozi_android.data.remote.network.DataResult
import com.example.pozi_android.data.remote.network.PBListRes
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

    private val _photoBoothList: MutableLiveData<DataResult<List<PB>>> =
        MutableLiveData()
    val photoBoothList: LiveData<DataResult<List<PB>>> get() = _photoBoothList



    fun getCenterList() {
        CoroutineScope(Dispatchers.IO).launch {
            val result = serviceRepository.getPBList()

            _photoBoothList.postValue(serviceRepository.getPBList())
        }
    }

//    fun getCenterList() = viewModelScope.launch {
//        _photoBoothList.postValue(serviceRepository.getPhotoBoothList())
//    }
}