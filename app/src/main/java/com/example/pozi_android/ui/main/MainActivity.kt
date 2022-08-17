package com.example.pozi_android.ui.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.UiThread
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.pozi_android.R
import com.example.pozi_android.databinding.ActivityMainBinding
import com.example.pozi_android.databinding.SelectMapApplicationBottomSheetBinding
import com.example.pozi_android.domain.entity.Place
import com.example.pozi_android.ui.base.BaseActivity
import com.example.pozi_android.ui.main.state.PBState
import com.example.pozi_android.ui.searchLocation.SearchLocationActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main),
    OnMapReadyCallback, Overlay.OnClickListener, PermissionListener {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var locationSource: FusedLocationSource
    lateinit var naverMap: NaverMap
    lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewPager: ViewPager2 by lazy {
        findViewById(R.id.ViewPager)
    }
    private val mapView: MapView by lazy {
        findViewById(R.id.mainmap)
    }

    private val viewPagerAdapter = MainPBInfoPagerAdapter().apply {
        findLoadClickListener = ::showMapAppList
    }

    private val searchLocationActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val latitude =
                    result.data?.getDoubleExtra(SearchLocationActivity.KEY_LATITUDE, 0.0)
                        ?: return@registerForActivityResult
                val longitude =
                    result.data?.getDoubleExtra(SearchLocationActivity.KEY_LONGITUDE, 0.0)
                        ?: return@registerForActivityResult
                val subTitle =
                    result.data?.getStringExtra(SearchLocationActivity.KEY_SUBTITLE) ?: ""

                binding.locationTxt.text = subTitle
                viewModel.moveCam(latitude, longitude)
            }
        }

    override fun initView() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        permissionCheck()
        mapView.getMapAsync(this)
        settingViewpager()
        setClickListener()
    }

    private fun setClickListener() {
        binding.currentbutton.setOnClickListener {
            currentAddress()
        }
        binding.searchLocationButton.setOnClickListener {
            searchLocationActivityLauncher.launch(
                Intent(this, SearchLocationActivity::class.java)
            )
        }
    }

    private fun settingViewpager() {
        viewPager.adapter = viewPagerAdapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            //viewpager에서 바뀔때마다 카메라가 전환된다, 마커도 검은색으로 색칠
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val selectedPB = viewPagerAdapter.currentList[position]
                viewModel.setFocusedPlace(selectedPB)
            }
        })
    }


    @UiThread
    override fun onMapReady(map: NaverMap) {
        viewModel.setMapClickListener(map)
        currentAddress()
        this.naverMap = map
        // 위치 추적 모드
        map.locationSource = locationSource

        viewModel.getAllPlace()

        lifecycleScope.launch {
            viewModel.placeListStateFlow.collect { uiState ->
                when (uiState) {
                    is PBState.Success -> {
                        viewModel.attachMarker(uiState.data, mapView, viewPager, viewPagerAdapter)
                        viewPagerAdapter.submitList(uiState.data.toMutableList())
                    }
                    is PBState.Error -> {
                        Log.d("임민규", "ERROR")
                    }
                }
            }
        }
    }

    private fun showMapAppList(place: Place?) {
        if (place == null) return
        val binding = SelectMapApplicationBottomSheetBinding.inflate(layoutInflater).apply {
            naverImage.setOnClickListener {
                val url = getString(
                    R.string.findLoad_naver_url,
                    place.marker.position.latitude.toString(),
                    place.marker.position.longitude.toString(),
                    place.address
                )
                executeMap(url)
            }
            kakaoImage.setOnClickListener {
                val url = getString(
                    R.string.findLoad_kakao_url,
                    place.marker.position.latitude.toString(),
                    place.marker.position.longitude.toString()
                )
                executeMap(url)
            }
        }
        BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme).apply {
            setContentView(binding.root)
        }.show()
    }

    private fun executeMap(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {  // 만약 실행이 안된다면 (앱이 없다면)
            Toast.makeText(this, "해당 앱이 설치되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun currentAddress() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this@MainActivity).apply {
                lastLocation.addOnSuccessListener { location: Location? ->
                    naverMap.locationOverlay.apply {
                        isVisible = true
                        position = LatLng(location!!.latitude, location.longitude)
                    }
                    naverMap.locationTrackingMode = LocationTrackingMode.Follow
                    binding.locationTxt.text = getAddress(location!!.latitude, location.longitude)
                }
            }
    }

    private fun getAddress(lat: Double, lng: Double): String {
        return try {
            val address = Geocoder(this, Locale.KOREA).getFromLocation(lat, lng, 1).firstOrNull()
            val fullAddress = address?.getAddressLine(0).toString()
            val countryLength = address?.countryName?.length ?: -1
            fullAddress.substring(countryLength + 1)
        } catch (e: IOException) {
            e.printStackTrace()
            "주소를 가져 올 수 없습니다."
        }
    }

    override fun onClick(overly: Overlay): Boolean {
        val selectedModel = viewPagerAdapter.currentList.firstOrNull {
            it.id == overly.tag
        }

        selectedModel?.let {
            val position = viewPagerAdapter.currentList.indexOf(it)
            viewPager.currentItem = position
        }

        return true
    }

    private fun permissionCheck() {
        TedPermission.create()
            .setPermissionListener(this)
            .setPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .check()
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onPermissionGranted() {
        Toast.makeText(this@MainActivity, "위치 정보 제공되었습니다", Toast.LENGTH_SHORT).show()
    }

    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
        Toast.makeText(this@MainActivity, "위치 정보 제공이 거부되었습니다.", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val TAG = "MainActivity"
    }
}