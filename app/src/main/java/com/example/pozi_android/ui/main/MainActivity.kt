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
import com.example.pozi_android.util.CustomMarkerUtil
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
import java.util.*


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main),
    OnMapReadyCallback, PermissionListener {

    private val viewModel by viewModels<MainViewModel>()
    lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var geocoder: Geocoder

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
        geocoder = Geocoder(this, Locale.KOREA)
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
            viewModel.getAddress(geocoder,it)
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
            CustomMarkerUtil.loseFocus(viewModel.focusedCustomMarker.value)
            viewModel.turnwigetVisible(false)
        }
    }

    private fun deleteAllMarker() {
        if (viewPagerAdapter.currentList != null) {
            val prevplacelist = viewPagerAdapter.currentList
            mapView.getMapAsync { naverMap ->
                prevplacelist.forEach { places ->
                    places.marker.map = null
                }
            }
        }
        viewModel.turnwigetVisible(false)
    }

    private fun attachMarker(list: List<CustomMarker>) {
        deleteAllMarker()
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

    fun markertoWiget(customMarker: CustomMarker) {
        viewModel.turnwigetVisible(true)
        val selectedModel = viewPagerAdapter.currentList.firstOrNull {
            it.id == customMarker.id
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
                    is PBState.NoData -> {
                        deleteAllMarker()
                    }
                    is PBState.Error -> {
                        Log.d("임민규", "에러")
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

    fun showMapAppList(item: CustomMarker?) {
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
            Toast.makeText(
                this,
                getString(R.string.googlePlayStore_guide_comment),
                Toast.LENGTH_SHORT
            ).show()
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
                .setDeniedMessage(getString(R.string.locationPermission_guide_comment))
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
                    viewModel.getAddress(geocoder,currentLocation)
                }
            }
    }

    fun permissionCheck() {
        TedPermission.create()
            .setPermissionListener(this)
            .setDeniedMessage(getString(R.string.locationPermission_guide_comment))
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
        viewModel.currentPositionListener(LatLng(gangnamLat, gangnamLng))
        Toast.makeText(
            this@MainActivity,
            getString(R.string.permission_reject_guide_comment),
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
        private const val TAG = "MainActivity"
        private const val gangnamLat = 37.497885
        private const val gangnamLng = 127.027512
    }
}