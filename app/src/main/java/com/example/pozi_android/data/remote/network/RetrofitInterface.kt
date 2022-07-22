package com.example.pozi_android.data.remote.network

import com.example.pozi_android.data.remote.spec.TestRes
import com.example.pozi_android.data.remote.url.LocationsUrl
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitInterface {
    @GET(LocationsUrl.LOCATION_URL)
    suspend fun getPhotoBoothList(): Response<TestRes>
}