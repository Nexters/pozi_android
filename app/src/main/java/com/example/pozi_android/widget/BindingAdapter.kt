package com.example.pozi_android.widget

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.pozi_android.domain.entity.Place
import com.example.pozi_android.ui.main.state.PBState
import com.example.pozi_android.ui.main.state.PBState.Success
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*

@BindingAdapter("settext")
fun setText(view: TextView, text: String) {
    view.text = text
}

@BindingAdapter("setImage")
fun setImage(imageview: ImageView, drawable: Int) {
    Glide.with(imageview.context).load(drawable).into(imageview)
}

@BindingAdapter("setdisttext")
fun setdisttext(view: TextView, text: Long?) {
    if (text == null) view.text = "123m"
    else view.text = text.toString() + "m"
}

@BindingAdapter("naverlogoposition")
fun naverlogoposition(mapView: MapView, isVisible: Boolean) {
    mapView.getMapAsync { naverMap ->
        if(isVisible){
            naverMap.uiSettings.setLogoMargin(65,0,0,750)
        }else{
            naverMap.uiSettings.setLogoMargin(65,0,0,50)
        }
    }
}

@BindingAdapter("attachmarker", "onClickPlace")
fun attachmarker(mapView: MapView, list: List<Place>, onClickPlace: (Place) -> Unit) {
    mapView.getMapAsync { naverMap ->
        list.forEach { place ->
            place.marker.setOnClickListener {
                onClickPlace(place)
                true
            }
            place.marker.map = naverMap
        }
    }
}

@BindingAdapter("moveCamera")
fun moveCamera(mapView: MapView, latLng: LatLng?) {
    if (latLng != null) {
        mapView.getMapAsync { naverMap ->
            val cameraUpdate = CameraUpdate.scrollAndZoomTo(latLng, 16.0)
                .animate(CameraAnimation.Easing)
            naverMap.moveCamera(cameraUpdate)
        }
    } else {
        mapView.getMapAsync { naverMap ->
            val cameraUpdate = CameraUpdate.scrollAndZoomTo(LatLng(37.497885, 127.027512), 16.0)
                .animate(CameraAnimation.Easing)
            naverMap.moveCamera(cameraUpdate)
            naverMap.uiSettings.isCompassEnabled = false
            naverMap.uiSettings.isZoomControlEnabled = false
            naverMap.uiSettings.isScaleBarEnabled = false
            naverMap.uiSettings.isLogoClickEnabled = false
            //naverMap.uiSettings.logoGravity = Gravity.BOTTOM
        }
    }
}


