package com.example.pozi_android.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.UiThread
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.pozi_android.R
import com.example.pozi_android.databinding.ActivityMainBinding
import com.example.pozi_android.databinding.SelectMapApplicationBottomSheetBinding
import com.example.pozi_android.domain.entity.PBEntity
import com.example.pozi_android.ui.base.BaseActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
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

    override fun initView() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        mapView.getMapAsync(this)
        permissionCheck()

        settingViewpager()
        currentimage.setOnClickListener {
            currentAddress()
        }
    }

    private fun showMapAppList(pb: PBEntity?) {
        if (pb == null) return
        val binding = SelectMapApplicationBottomSheetBinding.inflate(layoutInflater).apply {
            naverImage.setOnClickListener {
                val url = "nmap://route/walk?dlat=${pb._latitude}&dlng=${pb._longitude}&dname=${pb.brandName}"
                executeMap(url)
            }
            kakaoImage.setOnClickListener {
                val url = "kakaomap://route?ep=${pb._latitude},${pb._longitude}&by=FOOT"
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

    private fun settingViewpager() {
        viewPager.adapter = viewPagerAdapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            //viewpager에서 바뀔때마다 카메라가 전환된다.
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val selectedPB = viewPagerAdapter.currentList[position]
                viewModel.markerClickListener(LatLng(selectedPB._latitude, selectedPB._longitude))
            }
        })
    }


    @UiThread
    override fun onMapReady(map: NaverMap) {

        viewModel.setMapClickListener(map)

        this.naverMap = map
        map.locationSource = locationSource

        viewModel.getCenterList()

        viewModel.markerList.observe(this) { markers ->
            markers.forEach { Marker ->
                Marker.setOnClickListener {
                    viewModel.markerClickListener(Marker.position)
                    true
                }
            }
//            CoroutineScope(Dispatchers.Main).launch {
//                markers.forEach { marker ->
//                    marker.map = naverMap
//                }
//            }
        }

        lifecycleScope.launch {
            viewModel.PBListStateFlow.collect { uiState ->
                when (uiState) {
                    is PBState.Success -> {
                        viewPagerAdapter.submitList(uiState.data.toMutableList())
                    }
                    is PBState.Error -> {
                        Log.d("임민규", "ERROR")
                    }
                }
            }
        }

        currentAddress()
    }


    fun currentAddress() {
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

    fun getAddress(lat: Double, lng: Double): String {
        val geoCoder = Geocoder(this, Locale.KOREA)
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

}