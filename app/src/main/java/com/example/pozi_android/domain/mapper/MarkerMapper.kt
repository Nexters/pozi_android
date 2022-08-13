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
                entity.brandName.contains("포토매틱") -> {
                    OverlayImage.fromResource(R.drawable.photomatic_off)
                }
                entity.brandName.contains("하루필름,") -> {
                    OverlayImage.fromResource(R.drawable.harufilm_off)
                }
                entity.brandName.contains("셀픽스") -> {
                    OverlayImage.fromResource(R.drawable.selfix_off)
                }
                entity.brandName.contains("포토드링크") -> {
                    OverlayImage.fromResource(R.drawable.photodrink_off)
                }
                entity.brandName.contains("포토그레이") -> {
                    OverlayImage.fromResource(R.drawable.photogray_off)
                }
                entity.brandName.contains("포토이즘") -> {
                    OverlayImage.fromResource(R.drawable.photoism_off)
                }
                else -> {
                    MarkerIcons.BLACK.also {
                        com.naver.maps.map.R.drawable.navermap_default_marker_icon_black
                    }
                }
            }

        }
}