package com.example.pozi_android.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import com.example.pozi_android.R
import com.example.pozi_android.ui.main.CustomMarker
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.launch

object CustomMarkerUtil {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun transMarker(customMarker: CustomMarker?) = coroutineScope.launch {
        customMarker?.marker?.width = 50
        customMarker?.marker?.height = 50
        customMarker?.marker?.icon = OverlayImage.fromResource(R.drawable.marker_small)
    }

    fun getFocus(customMarker: CustomMarker?) = coroutineScope.launch {
        customMarker?.marker?.width = 155
        customMarker?.marker?.height = 170
        when (customMarker?.brandName) {
            "포토매틱" -> {
                customMarker.marker.icon = OverlayImage.fromResource(R.drawable.marker_photomatic_on)
            }
            "하루필름" -> {
                customMarker.marker.icon = OverlayImage.fromResource(R.drawable.marker_harufilm_on)
            }
            "셀픽스" -> {
                customMarker.marker.icon = OverlayImage.fromResource(R.drawable.marker_selfix_on)
            }
            "포토드링크" -> {
                customMarker.marker.icon = OverlayImage.fromResource(R.drawable.marker_photodrink_on)
            }
            "포토그레이" -> {
                customMarker.marker.icon = OverlayImage.fromResource(R.drawable.marker_photogray_on)
            }
            "포토이즘" -> {
                customMarker.marker.icon = OverlayImage.fromResource(R.drawable.marker_photoism_on)
            }
            "인생네컷" -> {
                customMarker.marker.icon = OverlayImage.fromResource(R.drawable.marker_lifefourcut_on)
            }
            "비룸스튜디오" -> {
                customMarker.marker.icon = OverlayImage.fromResource(R.drawable.marker_broom_on)
            }
        }
    }

    fun loseFocus(customMarker: CustomMarker?) = coroutineScope.launch {
        customMarker?.marker?.width = 155
        customMarker?.marker?.height = 170
        when (customMarker?.brandName) {
            "포토매틱" -> {
                customMarker.marker.icon = OverlayImage.fromResource(R.drawable.marker_photomatic_off)
            }
            "하루필름" -> {
                customMarker.marker.icon = OverlayImage.fromResource(R.drawable.marker_harufilm_off)
            }
            "셀픽스" -> {
                customMarker.marker.icon = OverlayImage.fromResource(R.drawable.marker_selfix_off)
            }
            "포토드링크" -> {
                customMarker.marker.icon = OverlayImage.fromResource(R.drawable.marker_photodrink_off)
            }
            "포토그레이" -> {
                customMarker.marker.icon = OverlayImage.fromResource(R.drawable.marker_photogray_off)
            }
            "포토이즘" -> {
                customMarker.marker.icon = OverlayImage.fromResource(R.drawable.marker_photoism_off)
            }
            "인생네컷" -> {
                customMarker.marker.icon = OverlayImage.fromResource(R.drawable.marker_lifefourcut_off)
            }
            "비룸스튜디오" -> {
                customMarker.marker.icon = OverlayImage.fromResource(R.drawable.marker_broom_off)
            }
        }
    }

}