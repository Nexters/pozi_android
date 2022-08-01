package com.example.pozi_android.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.activity.viewModels
import com.example.pozi_android.R
import androidx.annotation.UiThread
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.pozi_android.databinding.ActivityMainBinding
import com.example.pozi_android.domain.entity.PB
import com.example.pozi_android.ui.base.BaseActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.MarkerIcons
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main),
    OnMapReadyCallback, Overlay.OnClickListener {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val viewPager: ViewPager2 by lazy { //이거임
        findViewById(R.id.ViewPager)
    }
    private val viewPagerAdapter = MainPBInfoPagerAdapter()

    override fun initView() {
        attachFragmentmanager()
        settingViewpager()
    }

    private fun settingViewpager() {
        viewPager.adapter = viewPagerAdapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            //viewpager에서 바뀔때마다 카메라가 전환된다.
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val selectedPB = viewPagerAdapter.currentList[position]
                val cameraUpdate =
                    CameraUpdate.scrollTo(LatLng(selectedPB._latitude, selectedPB._longitude))
                        .animate(CameraAnimation.Easing)

                naverMap.moveCamera(cameraUpdate)
            }

        })
    }

    private fun attachFragmentmanager() {
        val mapFragment = supportFragmentManager.run {
            // 옵션 설정
            val option = NaverMapOptions().mapType(NaverMap.MapType.Basic)
                .camera(CameraPosition(LatLng(37.530039, 126.926209), 15.0))
                .locationButtonEnabled(false)
            findFragmentById(R.id.mainmap) as MapFragment?
                ?: MapFragment.newInstance(option)
                    .also {
                        beginTransaction().add(R.id.mainmap, it).commit()
                    }
        }

        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        ) {
            if (!locationSource.isActivated) {
                Log.d(TAG, "MainActivity - onRequestPermissionsResult 권한 거부됨")
                naverMap.locationTrackingMode = LocationTrackingMode.None
            } else {
                Log.d(TAG, "MainActivity - onRequestPermissionsResult 권한 승인됨")
                naverMap.locationTrackingMode = LocationTrackingMode.Follow
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @UiThread
    override fun onMapReady(map: NaverMap) {
        map.locationSource = locationSource
        this.naverMap = map.apply {
            binding.btnLocation.map = this
        }
        this.naverMap.uiSettings.isCompassEnabled = false
        this.naverMap.uiSettings.isZoomControlEnabled = false
        this.naverMap.uiSettings.isScaleBarEnabled = false
        this.naverMap.uiSettings.isLogoClickEnabled = false

        viewModel.getCenterList()

        lifecycleScope.launch {
            viewModel.PBListStateFlow.collect { uiState ->
                when (uiState) {
                    is PBState.Success -> {
                        val markers = mutableListOf<Marker>()
                        CreateMarker(markers, uiState.data)
                        viewPagerAdapter.submitList(uiState.data.toMutableList())
                    }
                    is PBState.Error -> {
                        Log.d("임민규", "ERROR")
                    }
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        // 사용자 현재 위치 받아오기
        var currentLocation: Location?
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this@MainActivity).apply {
                lastLocation.addOnSuccessListener { location: Location? ->
                    currentLocation = location
                    naverMap.locationOverlay.run {
                        isVisible = true
                        position = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
                    }

                    // 카메라 현재위치로 이동
                    val cameraUpdate = CameraUpdate.scrollTo(
                        LatLng(
                            currentLocation!!.latitude,
                            currentLocation!!.longitude
                        )
                    )
                    with(naverMap) {
                        naverMap.moveCamera(cameraUpdate)
                        locationTrackingMode = LocationTrackingMode.Follow
                        binding.locationTxt.run {
                            text = getAddress(
                                currentLocation!!.latitude,
                                currentLocation!!.longitude
                            )
                        }
                    }
                }
            }
    }

    //usecase,repository 에서 결과값을 보내주는 느낌이 좋은듯 -> 이창
    fun getAddress(lat: Double, lng: Double): String {
        val geoCoder = Geocoder(this,Locale.KOREA)
        val address: ArrayList<Address>
        var addressResult = "주소를 가져 올 수 없습니다."
        try {
            address = geoCoder.getFromLocation(lat, lng, 1) as ArrayList<Address>
            if (address.size > 0) {
                // 주소 받아오기
                val currentLocationAddress = address[0].getAddressLine(0)
                    .toString()
                addressResult = currentLocationAddress

            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressResult
    }

    //databinding 하면 좋겠음
    private fun CreateMarker(markers: MutableList<Marker>, locations: List<PB>) {
        locations.forEach { it ->
            markers += Marker().apply {
                position = LatLng(it._latitude, it._longitude)
                tag = it.id
                onClickListener = this@MainActivity
                isHideCollidedSymbols = true
                isIconPerspectiveEnabled = true
                width = 155
                height = 170
                // 아이콘 설정
                icon = when {
                    it.brandName.contains("인생네컷") -> {
                        OverlayImage.fromResource(R.drawable.lifefourcut_off)
                    }
                    it.brandName.contains("포토매틱") -> {
                        OverlayImage.fromResource(R.drawable.photomatic_off)
                    }
                    else -> {
                        MarkerIcons.BLACK.also {
                            com.naver.maps.map.R.drawable.navermap_default_marker_icon_black
                        }
                    }
                }

            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            markers.forEach { marker ->
                marker.map = naverMap
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val TAG = "MainActivity"
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

}