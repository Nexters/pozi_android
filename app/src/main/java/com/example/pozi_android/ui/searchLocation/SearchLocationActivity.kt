package com.example.pozi_android.ui.searchLocation

import android.content.Intent
import androidx.activity.viewModels
import com.example.pozi_android.R
import com.example.pozi_android.databinding.ActivitySearchLocationBinding
import com.example.pozi_android.ui.base.BaseActivity
import com.example.pozi_android.ui.main.MainActivity

class SearchLocationActivity :
    BaseActivity<ActivitySearchLocationBinding>(R.layout.activity_search_location) {

    override fun initView() {
        binding.lifecycleOwner = this
    }

}