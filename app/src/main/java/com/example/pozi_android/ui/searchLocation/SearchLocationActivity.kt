package com.example.pozi_android.ui.searchLocation

import android.content.Intent
import android.location.Geocoder
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.pozi_android.R
import com.example.pozi_android.databinding.ActivitySearchLocationBinding
import com.example.pozi_android.ui.base.BaseActivity
import com.example.pozi_android.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class SearchLocationActivity :
    BaseActivity<ActivitySearchLocationBinding>(R.layout.activity_search_location) {

    private val viewModel by viewModels<SearchLocationViewModel>()

    private lateinit var addressAdapter: SearchLocationAdapter
    private lateinit var geocoder: Geocoder

    override fun initView() {
        binding.lifecycleOwner = this
        geocoder = Geocoder(this, Locale.KOREA)
        setRecyclerView()
        setListeners()
        setObservers()
    }

    private fun setListeners() {
        binding.searchText.addTextChangedListener {
            viewModel.getAddress(geocoder, it.toString())
        }
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.addressList.collectLatest {
                addressAdapter.updateList(it)
            }
        }
    }

    private fun setRecyclerView() {
        addressAdapter = SearchLocationAdapter(
            emptyList()
        ) {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra(KEY_LATITUDE, it.latitude)
                putExtra(KEY_LONGITUDE, it.longitude)
                putExtra(KEY_SUBTITLE, it.subTitle)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
        binding.searchRV.adapter = addressAdapter
    }

    companion object {
        const val KEY_LATITUDE = "KEY_LATITUDE"
        const val KEY_LONGITUDE = "KEY_LONGITUDE"
        const val KEY_SUBTITLE = "KEY_SUBTITLE"
    }
}