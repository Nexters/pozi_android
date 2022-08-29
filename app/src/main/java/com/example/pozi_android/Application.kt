package com.example.pozi_android

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDialog
import com.bumptech.glide.Glide
import com.naver.maps.map.NaverMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        APPLICATION_CONTEXT = applicationContext

        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.Client_ID)
    }

    companion object {
        private lateinit var APPLICATION_CONTEXT: Context

        @JvmStatic
        fun getContext(): Context {
            return APPLICATION_CONTEXT
        }
    }
}