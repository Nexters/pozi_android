package com.example.pozi_android.data.remote.network

import com.example.pozi_android.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response


class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
                .addHeader("X-NCP-APIGW-API-KEY-ID", BuildConfig.API_KEY)
                .addHeader("X-NCP-APIGW-API-KEY", BuildConfig.Client_Secret)
                .build()
        )
    }
}