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

    fun getFocus(place: Place?) = coroutineScope.launch {
        when (place?.brandName) {
            "포토매틱" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.photomatic_on)
            }
            "하루필름" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.harufilm_on)
            }
            "셀픽스" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.selfix_on)
            }
            "포토드링크" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.photodrink_on)
            }
            "포토그레이" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.photogray_on)
            }
            "포토이즘" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.photoism_on)
            }
            "인생네컷" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.lifefourcut_on)
            }
            "비룸" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.broom_on)
            }
            else -> {
                MarkerIcons.RED.also {
                    com.naver.maps.map.R.drawable.navermap_default_marker_icon_red
                }
            }
        }
    }

    fun loseFocus(place: Place?) = coroutineScope.launch {
        when (place?.brandName) {
            "포토매틱" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.photomatic_off)
            }
            "하루필름" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.harufilm_off)
            }
            "셀픽스" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.selfix_off)
            }
            "포토드링크" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.photodrink_off)
            }
            "포토그레이" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.photogray_off)
            }
            "포토이즘" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.photoism_off)
            }
            "인생네컷" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.lifefourcut_off)
            }
            "비룸" -> {
                place.marker.icon = OverlayImage.fromResource(R.drawable.broom_off)
            }
            else -> {
                MarkerIcons.BLACK.also {
                    com.naver.maps.map.R.drawable.navermap_default_marker_icon_black
                }
            }
        }
    }

}