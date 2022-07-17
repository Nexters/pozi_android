package com.example.pozi_android.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.util.Log
import androidx.activity.viewModels
import com.example.pozi_android.R
import androidx.annotation.UiThread
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.pozi_android.data.remote.network.Status
import com.example.pozi_android.databinding.ActivityMainBinding
import com.example.pozi_android.ui.base.BaseActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main),
    OnMapReadyCallback, Overlay.OnClickListener {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun initView() {
        attachFragmentmanager()
    }

    fun attachFragmentmanager() {
        val mapFragment = supportFragmentManager.run {
            // 옵션 설정
            val option = NaverMapOptions().mapType(NaverMap.MapType.Basic)
                .camera(CameraPosition(LatLng(37.530039, 126.926209), 16.0))
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

        viewModel.getCenterList()


        //databinding으로 연결해서 바로 받아올수있게 한다.
        viewModel.photoBoothList.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (!it.data!!.locations.isNullOrEmpty()) { //성공
                        val markers = mutableListOf<Marker>()
                        it.data.locations.forEach { it ->
                            markers += Marker().apply {
                                position = LatLng(it.lat.toDouble(), it.lng.toDouble())
                                isHideCollidedSymbols = true
                                isIconPerspectiveEnabled = true
                                // 아이콘 설정
                                icon = if (it.name.contains("인생네컷")) { //고려사항
                                    MarkerIcons.GREEN.also {
                                        com.naver.maps.map.R.drawable.navermap_default_marker_icon_green
                                    }
                                } else {
                                    MarkerIcons.BLUE.also {
                                        com.naver.maps.map.R.drawable.navermap_default_marker_icon_blue
                                    }
                                }
                            }
                        }

                        CoroutineScope(Dispatchers.Main).launch {
                            markers.forEach { marker ->
                                marker.map = naverMap
                            }
                        }
                        Log.d("임민규", it.data.locations.toString())
                    } else { //값이 없을때
                        Log.d("임민규", "else")
                    }
                }
                Status.ERROR -> {
                    Log.d("임민규", "ERROR")
                }
                Status.LOADING -> {
                    Log.d("임민규", "LOADING")
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
                    // 위치 오버레이의 가시성은 기본적으로 false로 지정되어 있습니다. 가시성을 true로 변경하면 지도에 위치 오버레이가 나타납니다.
                    // 파랑색 점, 현재 위치 표시
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
                    }
                }
            }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val TAG = "MainActivity"
    }

    override fun onClick(overly: Overlay): Boolean {
        TODO("Not yet implemented")
    }
}