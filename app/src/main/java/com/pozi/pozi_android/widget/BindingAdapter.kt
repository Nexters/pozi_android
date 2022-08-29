package com.pozi.pozi_android.widget

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.pozi.pozi_android.ui.main.state.PBState
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*

@BindingAdapter("settext")
fun setText(view: TextView, text: String) {
    view.text = text
}

@BindingAdapter("noDataVisible")
fun noDataVisible(view: View, pbState: PBState) {
    when (pbState) {
        is PBState.NoData -> {
            view.visibility = View.VISIBLE
        }
        else -> {
            view.visibility = View.GONE
        }
    }
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
        if (isVisible) {
            naverMap.uiSettings.setLogoMargin(65, 0, 0, 750)
        } else {
            naverMap.uiSettings.setLogoMargin(65, 0, 0, 50)
        }
    }
}

@BindingAdapter("moveCamera")
fun moveCamera(mapView: MapView, latLng: LatLng) {
    mapView.getMapAsync { naverMap ->
        val cameraUpdate = CameraUpdate.scrollAndZoomTo(latLng, 16.0)
            .animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)
    }
}


