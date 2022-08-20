package com.example.pozi_android.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import com.example.pozi_android.R
import com.example.pozi_android.domain.entity.Place
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.MarkerIcons
import kotlinx.coroutines.launch

object PlaceUtil {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun transMarker(place: Place?) = coroutineScope.launch {
        place?.marker?.width = 50
        place?.marker?.height = 50
        place?.marker?.icon = OverlayImage.fromResource(R.drawable.marker_small)
    }

    fun getFocus(place: Place?) = coroutineScope.launch {
        place?.marker?.width = 155
        place?.marker?.height = 170
        when (place?.brandName) {
            "포토매틱" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.marker_photomatic_on)
            }
            "하루필름" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.marker_harufilm_on)
            }
            "셀픽스" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.marker_selfix_on)
            }
            "포토드링크" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.marker_photodrink_on)
            }
            "포토그레이" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.marker_photogray_on)
            }
            "포토이즘" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.marker_photoism_on)
            }
            "인생네컷" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.marker_lifefourcut_on)
            }
            "비룸" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.marker_broom_on)
            }
            else -> {
                MarkerIcons.RED.also {
                    com.naver.maps.map.R.drawable.navermap_default_marker_icon_red
                }
            }
        }
    }

    fun loseFocus(place: Place?) = coroutineScope.launch {
        place?.marker?.width = 155
        place?.marker?.height = 170
        when (place?.brandName) {
            "포토매틱" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.marker_photomatic_off)
            }
            "하루필름" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.marker_harufilm_off)
            }
            "셀픽스" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.marker_selfix_off)
            }
            "포토드링크" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.marker_photodrink_off)
            }
            "포토그레이" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.marker_photogray_off)
            }
            "포토이즘" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.marker_photoism_off)
            }
            "인생네컷" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.marker_lifefourcut_off)
            }
            "비룸" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.marker_broom_off)
            }
            else -> {
                MarkerIcons.BLACK.also {
                    com.naver.maps.map.R.drawable.navermap_default_marker_icon_black
                }
            }
        }
    }

}