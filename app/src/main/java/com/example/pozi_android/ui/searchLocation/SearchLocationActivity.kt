package com.example.pozi_android.ui.searchLocation

import android.content.Intent
import android.location.Geocoder
import android.opengl.Visibility
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.pozi_android.R
import com.example.pozi_android.databinding.ActivitySearchLocationBinding
import com.example.pozi_android.ui.base.BaseActivity
import com.example.pozi_android.ui.extension.textChangesToFlow
import com.example.pozi_android.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class SearchLocationActivity :
    BaseActivity<ActivitySearchLocationBinding>(R.layout.activity_search_location) {

    private val viewModel by viewModels<SearchLocationViewModel>()

    private lateinit var addressAdapter: SearchLocationAdapter

    override fun initView() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setRecyclerView()
        setListeners()
        setObservers()
        setClickListener()
    }

    private fun setClickListener() {
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.clearButton.setOnClickListener {
            binding.searchText.text = null
            binding.clearButton.visibility = View.GONE
        }
    }

    @OptIn(FlowPreview::class)
    private fun setListeners() {
        binding.searchText.textChangesToFlow()
            .debounce(500)
            .filter {
                it?.isBlank() != true
            }
            .onEach {
                binding.clearButton.visibility = View.VISIBLE
                viewModel.getAddress(it.toString())
            }
            .launchIn(lifecycleScope)
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