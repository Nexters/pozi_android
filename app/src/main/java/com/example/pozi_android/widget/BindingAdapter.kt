package com.example.pozi_android.widget

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.pozi_android.R
import com.example.pozi_android.ui.main.MainViewModel
import com.example.pozi_android.ui.main.PBState
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.MarkerIcons

@BindingAdapter("settext")
fun setText(view: TextView, text: String) {
    view.text = text
}

@BindingAdapter("visible")
fun setVisible(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("moveCamera")
fun moveCamera(mapView: MapView, latLng: LatLng?) {
    if (latLng != null) {
        mapView.getMapAsync { naverMap ->
            val cameraUpdate = CameraUpdate.scrollAndZoomTo(latLng, 15.0)
                .animate(CameraAnimation.Easing)
            naverMap.moveCamera(cameraUpdate)
        }
    } else {
        mapView.getMapAsync { naverMap ->
            val cameraUpdate = CameraUpdate.scrollAndZoomTo(LatLng(37.497885, 127.027512), 15.0)
                .animate(CameraAnimation.Easing)
            naverMap.moveCamera(cameraUpdate)
            naverMap.uiSettings.isCompassEnabled = false
            naverMap.uiSettings.isZoomControlEnabled = false
            naverMap.uiSettings.isScaleBarEnabled = false
            naverMap.uiSettings.isLogoClickEnabled = false
        }
    }
}

@BindingAdapter("attachMarker")
fun attachMarker(mapView: MapView, markers: List<Marker>?) {
    if (markers != null) {
        mapView.getMapAsync { naverMap ->
            markers.forEach { marker ->
                marker.map = naverMap
            }
        }
    }
}

