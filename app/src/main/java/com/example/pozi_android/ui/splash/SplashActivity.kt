package com.example.pozi_android.ui.splash

import android.Manifest
import android.app.AlertDialog
import android.widget.Toast
import androidx.activity.viewModels
import com.example.pozi_android.R
import com.example.pozi_android.databinding.ActivitySplashBinding
import com.example.pozi_android.ui.base.BaseActivity
import com.example.pozi_android.ui.main.MainActivity
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import com.bumptech.glide.Glide

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    override fun initView() {
        setImage()
        postDelay()
    }

    fun setImage(){
        binding.appiconimage = R.drawable.appicon
    }

    fun postDelay() {
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }, DURATION)
    }

    companion object {
        private const val TAG = "SplashActivity"
        private const val DURATION: Long = 2000
    }

}