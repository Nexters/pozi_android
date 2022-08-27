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
import com.example.pozi_android.ui.base.BaseActivity
import com.example.pozi_android.ui.main.state.PBState
import com.example.pozi_android.ui.searchLocation.SearchLocationActivity
import com.example.pozi_android.util.PlaceUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.CameraUpdate.REASON_GESTURE
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main),
    OnMapReadyCallback, PermissionListener {

    private val viewModel by viewModels<MainViewModel>()
    lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
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
                viewModel.currentCamera(LatLng(latitude, longitude))
                viewModel.currentPositionListener(LatLng(latitude, longitude))
            }
        }

    override fun initView() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setImage()
        currentPosition()
        permissionCheck()
        mapView.getMapAsync(this)
        getPBList()
        settingViewpager()
        setClickListener()
    }

    private fun currentPosition() {
        viewModel.currentLatlng.observe(this) {
            viewModel.setGeoposition(getAddress(it))
            viewModel.getPBListChangeAdress()
        }
    }

    private fun setImage() {
        binding.currentimage = R.drawable.mylocation
    }

    private fun setClickListener() {
        binding.currentbutton.setOnClickListener {
            listenerCurrentPostion()
        }
        binding.searchLocationButton.setOnClickListener {
            searchLocationActivityLauncher.launch(
                Intent(this, SearchLocationActivity::class.java)
            )
        }
        binding.upToDataBtn.setOnClickListener {
            mapView.getMapAsync {
                viewModel.currentPositionListener(it.cameraPosition.target)
            }
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
                viewModel.onPlaceClick(selectedPB)
            }
        })
    }

    fun setMapClickListener(map: NaverMap) {
        naverMap.setOnMapClickListener { _, coord ->
            PlaceUtil.loseFocus(viewModel.focusedPlace.value)
            viewModel.turnwigetVisible(false)
        }
    }

    private fun attachMarker(list: List<Place>) {
        if (viewPagerAdapter.currentList != null) {
            val prevplacelist = viewPagerAdapter.currentList
            mapView.getMapAsync { naverMap ->
                prevplacelist.forEach { places ->
                    places.marker.map = null
                }
            }
        }
        mapView.getMapAsync { naverMap ->
            list.forEach { place ->
                place.marker.setOnClickListener {
                    viewModel.onPlaceClick(place)
                    markertoWiget(place)
                    true
                }
                place.marker.map = naverMap
            }
        }
    }

    fun markertoWiget(place: Place) {
        viewModel.turnwigetVisible(true)
        val selectedModel = viewPagerAdapter.currentList.firstOrNull {
            it.id == place.id
        }
        selectedModel?.let {
            val position = viewPagerAdapter.currentList.indexOf(it)
            viewPager.currentItem = position
        }
        viewModel.turnwigetVisible(true)
    }

    fun getPBList() {
        lifecycleScope.launch {
            viewModel.placeListStateFlow.collect { uiState ->
                when (uiState) {
                    is PBState.Success -> {
                        attachMarker(uiState.data)
                        viewPagerAdapter.submitList(uiState.data)
                    }
                    is PBState.Error -> {
                        Log.d("임민규", "ERROR")
                    }
                }
            }
        }
    }

    @UiThread
    override fun onMapReady(map: NaverMap) {
        this.naverMap = map
        map.locationSource = locationSource
        viewModel.setZoom(naverMap.cameraPosition.zoom)
        setMapClickListener(map)
        //viewModel.getPBListChangeAdress()

        naverMap.addOnCameraChangeListener { reason, animated ->
            if (reason == REASON_GESTURE) {
                if (viewModel.zoomCamera.value != naverMap.cameraPosition.zoom) {
                    when {
                        naverMap.cameraPosition.zoom < 13.0 -> {
                            lifecycleScope.launch {
                                viewModel.outZoom()
                                viewModel.setZoom(naverMap.cameraPosition.zoom)
                            }
                        }
                        naverMap.cameraPosition.zoom >= 15.0 -> {
                            lifecycleScope.launch {
                                viewModel.inZoom()
                                viewModel.setZoom(naverMap.cameraPosition.zoom)
                            }
                        }
                    }
                }
            }
        }

    }

    fun showMapAppList(item: Place?) {
        if (item == null) return
        var id: Int
        val binding = SelectMapApplicationBottomSheetBinding.inflate(layoutInflater).apply {
            naverImage.setOnClickListener {
                val url = getString(
                    R.string.findLoad_naver_url,
                    item.marker.position.latitude.toString(),
                    item.marker.position.longitude.toString(),
                    item.address
                )
                id = 0
                executeMap(url, id)
            }
            kakaoImage.setOnClickListener {
                val url = getString(
                    R.string.findLoad_kakao_url,
                    item.marker.position.latitude.toString(),
                    item.marker.position.longitude.toString()
                )
                id = 1
                executeMap(url, id)
            }
        }
        BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme).apply {
            setContentView(binding.root)
        }.show()
    }

    fun executeMap(url: String, id: Int) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "해당 앱이 설치되어 있지 않으므로 플레이스토어로 이동합니다.", Toast.LENGTH_SHORT).show()
            if (id == 0) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.googlePlayStore_naver_url))
                )
                startActivity(intent)
            } else {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.googlePlayStore_kakao_url))
                )
                startActivity(intent)
            }
        }
    }

    fun listenerCurrentPostion() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            TedPermission.create()
                .setPermissionListener(this)
                .setDeniedMessage("지도,길찾기 사용을 위해\n[설정] > [권한] 을 허용해주세요.(필수권한)")
                .setPermissions(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                .setGotoSettingButton(true)
                .check()

            return
        }

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this@MainActivity).apply {
                lastLocation.addOnSuccessListener { location: Location? ->
                    val currentLocation = LatLng(location!!.latitude, location!!.longitude)
                    naverMap.locationOverlay.run {
                        isVisible = true
                        position = currentLocation
                    }
                    with(naverMap) {
                        locationTrackingMode = LocationTrackingMode.Follow
                    }
                    viewModel.currentCamera(currentLocation)
                    viewModel.setGeoposition(getAddress(currentLocation))
                }
            }
    }

    fun getAddress(latLng: LatLng): String {
        return try {
            val address =
                Geocoder(this, Locale.KOREA).getFromLocation(latLng.latitude, latLng.longitude, 1)
                    .firstOrNull()
            val fullAddress = address?.getAddressLine(0).toString()
            val countryLength = address?.countryName?.length ?: -1
            fullAddress.substring(countryLength + 1)
        } catch (e: IOException) {
            e.printStackTrace()
            "주소를 가져 올 수 없습니다."
        }
    }

    fun permissionCheck() {
        TedPermission.create()
            .setPermissionListener(this)
            .setDeniedMessage("지도,길찾기 사용을 위해\n[설정] > [권한] 을 허용해주세요.(필수권한)")
            .setPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .setGotoSettingButton(true)
            .check()

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

    }

    override fun onPermissionGranted() {
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
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this@MainActivity).apply {
                lastLocation.addOnSuccessListener { location: Location? ->
                    val currentLocation = LatLng(location!!.latitude, location!!.longitude)
                    naverMap.locationOverlay.run {
                        isVisible = true
                        position = currentLocation
                    }
                    with(naverMap) {
                        locationTrackingMode = LocationTrackingMode.Follow
                    }
                    viewModel.currentCamera(currentLocation)
                    viewModel.currentPositionListener(currentLocation)
                }
            }
    }

    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
        viewModel.currentPositionListener(LatLng(Gangnam_Lat, Gangnam_Lng))
        Toast.makeText(this@MainActivity, "위치 정보 제공이 거부되었습니다.", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val TAG = "MainActivity"
        private const val Gangnam_Lat = 37.497885
        private const val Gangnam_Lng = 127.027512
    }
}