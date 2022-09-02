package com.pozi.pozi_android.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import com.pozi.pozi_android.R
import com.pozi.pozi_android.ui.main.model.CustomMarkerModel
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.launch

object CustomMarkerUtil {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun transMarker(customMarkerModel: CustomMarkerModel?) = coroutineScope.launch {
        customMarkerModel?.marker?.width = 70
        customMarkerModel?.marker?.height = 70
        customMarkerModel?.marker?.icon = OverlayImage.fromResource(R.drawable.marker_s)
    }

    fun getFocus(customMarkerModel: CustomMarkerModel?) = coroutineScope.launch {
        customMarkerModel?.marker?.zIndex = 100
        customMarkerModel?.marker?.width = 155
        customMarkerModel?.marker?.height = 170
        when (customMarkerModel?.brandName) {
            "포토매틱" -> {
                customMarkerModel.marker.icon = OverlayImage.fromResource(R.drawable.marker_photomatic_on)
            }
            "하루필름" -> {
                customMarkerModel.marker.icon = OverlayImage.fromResource(R.drawable.marker_harufilm_on)
            }
            "셀픽스" -> {
                customMarkerModel.marker.icon = OverlayImage.fromResource(R.drawable.marker_selfix_on)
            }
            "포토드링크" -> {
                customMarkerModel.marker.icon = OverlayImage.fromResource(R.drawable.marker_photodrink_on)
            }
            "포토그레이" -> {
                customMarkerModel.marker.icon = OverlayImage.fromResource(R.drawable.marker_photogray_on)
            }
            "포토이즘" -> {
                customMarkerModel.marker.icon = OverlayImage.fromResource(R.drawable.marker_photoism_on)
            }
            "인생네컷" -> {
                customMarkerModel.marker.icon = OverlayImage.fromResource(R.drawable.marker_lifefourcut_on)
            }
            "비룸스튜디오" -> {
                customMarkerModel.marker.icon = OverlayImage.fromResource(R.drawable.marker_broom_on)
            }
        }
    }

    fun loseFocus(customMarkerModel: CustomMarkerModel?) = coroutineScope.launch {
        customMarkerModel?.marker?.zIndex = 0
        customMarkerModel?.marker?.width = 155
        customMarkerModel?.marker?.height = 170
        when (customMarkerModel?.brandName) {
            "포토매틱" -> {
                customMarkerModel.marker.icon = OverlayImage.fromResource(R.drawable.marker_photomatic_off)
            }
            "하루필름" -> {
                customMarkerModel.marker.icon = OverlayImage.fromResource(R.drawable.marker_harufilm_off)
            }
            "셀픽스" -> {
                customMarkerModel.marker.icon = OverlayImage.fromResource(R.drawable.marker_selfix_off)
            }
            "포토드링크" -> {
                customMarkerModel.marker.icon = OverlayImage.fromResource(R.drawable.marker_photodrink_off)
            }
            "포토그레이" -> {
                customMarkerModel.marker.icon = OverlayImage.fromResource(R.drawable.marker_photogray_off)
            }
            "포토이즘" -> {
                customMarkerModel.marker.icon = OverlayImage.fromResource(R.drawable.marker_photoism_off)
            }
            "인생네컷" -> {
                customMarkerModel.marker.icon = OverlayImage.fromResource(R.drawable.marker_lifefourcut_off)
            }
            "비룸스튜디오" -> {
                customMarkerModel.marker.icon = OverlayImage.fromResource(R.drawable.marker_broom_off)
            }
        }
    }

}