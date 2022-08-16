package com.example.pozi_android.domain.mapper

import com.example.pozi_android.R
import com.example.pozi_android.domain.entity.CustomMarker
import com.example.pozi_android.domain.entity.PBEntity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.MarkerIcons

object MarkerMapper {
    fun entityToCustomMarker(entity: PBEntity): CustomMarker = CustomMarker(
        entityToMarker(entity),
        entity.address,
        entity.subject,
        entity.brandName,
        entity.phoneNumber
    )

    fun entityToMarker(entity: PBEntity): Marker =
        Marker().apply {
            position = LatLng(entity._latitude, entity._longitude)
            tag = entity.id
            isHideCollidedSymbols = true
            isIconPerspectiveEnabled = true
            width = 155
            height = 170
            icon = when (entity.brandName) {
                "포토매틱" -> {
                    OverlayImage.fromResource(R.drawable.photomatic_off)
                }
                "하루필름" -> {
                    OverlayImage.fromResource(R.drawable.harufilm_off)
                }
                "셀픽스" -> {
                    OverlayImage.fromResource(R.drawable.selfix_off)
                }
                "포토드링크" -> {
                    OverlayImage.fromResource(R.drawable.photodrink_off)
                }
                "포토그레이" -> {
                    OverlayImage.fromResource(R.drawable.photogray_off)
                }
                "포토이즘" -> {
                    OverlayImage.fromResource(R.drawable.photoism_off)
                }
                "인생네컷" -> {
                    OverlayImage.fromResource(R.drawable.lifefourcut_off)
                }
                "비룸" -> {
                    OverlayImage.fromResource(R.drawable.broom_off)
                }
                else -> {
                    MarkerIcons.BLACK.also {
                        com.naver.maps.map.R.drawable.navermap_default_marker_icon_black
                    }
                }
            }

        }
}