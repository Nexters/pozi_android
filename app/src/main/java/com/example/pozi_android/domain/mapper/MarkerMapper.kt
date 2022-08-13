package com.example.pozi_android.domain.mapper

import com.example.pozi_android.R
import com.example.pozi_android.domain.entity.PBEntity
import com.example.pozi_android.ui.main.MainViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.MarkerIcons

object MarkerMapper {
    fun EntityToMarker(entity: PBEntity): Marker =
        Marker().apply {
            position = LatLng(entity._latitude, entity._longitude)
            tag = entity.id
            isHideCollidedSymbols = true
            isIconPerspectiveEnabled = true
            width = 155
            height = 170
            icon = when {
                entity.brandName.contains("인생네컷") -> {
                    OverlayImage.fromResource(R.drawable.lifefourcut_off)
                }
                entity.brandName.contains("포토매틱") -> {
                    OverlayImage.fromResource(R.drawable.photomatic_off)
                }
                else -> {
                    MarkerIcons.BLACK.also {
                        com.naver.maps.map.R.drawable.navermap_default_marker_icon_black
                    }
                }
            }

        }
}