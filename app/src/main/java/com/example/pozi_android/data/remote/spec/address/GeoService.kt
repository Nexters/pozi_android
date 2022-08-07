package com.example.pozi_android.data.remote.spec.address

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface GeoService {
    @Headers(
        "X-NCP-APIGW-API-KEY-ID: p5grw6ifb4",
        "X-NCP-APIGW-API-KEY: MkdHn0cklyVS0klIErzzk0RXnPHlacpoI0LyCTxo"
    )
    @GET("gc")
    fun getAddress(
        @Query("coord") coord: String,
        // 좌표 체계: UTM-K
        @Query("sourcecrs") sourcecrs: String = "nhn:2048",
        // 변환 작업: 좌표 -> 법정동 & 도로명 주소
        @Query("orders") orders: String = "legalcode,roadaddr",
        @Query("output") output: String = "json"
    ): Response<GeoResponse>

}