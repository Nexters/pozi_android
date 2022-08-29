package com.pozi.pozi_android.ui.splash

import com.pozi.pozi_android.R
import com.pozi.pozi_android.databinding.ActivitySplashBinding
import com.pozi.pozi_android.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import android.content.Intent
import android.os.Handler
import com.pozi.pozi_android.ui.main.MainActivity

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