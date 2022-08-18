package com.example.pozi_android.domain.mapper

import com.example.pozi_android.R
import com.example.pozi_android.data.remote.spec.PBRes
import com.example.pozi_android.domain.entity.Place
import com.google.firebase.firestore.DocumentSnapshot
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.MarkerIcons

object PlaceMapper {

    fun mapperToPlace(id: Long, snap: DocumentSnapshot): Place =
        Place(
            id = id,
            marker = mapperToMarker(snap),
            address = snap.get("address") as String,
            subject = snap.get("subject") as String,
            brandName = snap.get("brandName") as String
        )

    fun mapperToPlace(id: Long, pb: PBRes.PB): Place =
        Place(
            id = id,
            marker = mapperToMarker(pb),
            address = pb.address,
            subject = pb.subject,
            brandName = pb.brandName,
        )

    fun mapperToMarker(snap: DocumentSnapshot): Marker =
        Marker().apply {
            var latlng: Map<String, Double>? = snap.get("coordinates") as Map<String, Double>?
            if(latlng == null){
                position = LatLng(
                    snap.get("posX") as Double,
                    snap.get("posY") as Double
                )
            }else{
                position = LatLng(
                    latlng.get("_latitude") as Double,
                    latlng.get("_longitude") as Double
                )
            }

            tag = snap.get("brandName") as String
            isHideCollidedSymbols = true
            isIconPerspectiveEnabled = true
            width = 155
            height = 170
            icon = when (snap.get("brandName") as String) {
                "포토매틱" -> {
                    OverlayImage.fromResource(R.drawable.marker_photomatic_off)
                }
                "하루필름" -> {
                    OverlayImage.fromResource(R.drawable.marker_harufilm_off)
                }
                "셀픽스" -> {
                    OverlayImage.fromResource(R.drawable.marker_selfix_off)
                }
                "포토드링크" -> {
                    OverlayImage.fromResource(R.drawable.marker_photodrink_off)
                }
                "포토그레이" -> {
                    OverlayImage.fromResource(R.drawable.marker_photogray_off)
                }
                "포토이즘" -> {
                    OverlayImage.fromResource(R.drawable.marker_photoism_off)
                }
                "인생네컷" -> {
                    OverlayImage.fromResource(R.drawable.marker_lifefourcut_off)
                }
                "비룸" -> {
                    OverlayImage.fromResource(R.drawable.marker_broom_off)
                }
                else -> {
                    MarkerIcons.BLACK.also {
                        com.naver.maps.map.R.drawable.navermap_default_marker_icon_black
                    }
                }
            }
        }

    fun mapperToMarker(pb: PBRes.PB): Marker =
        Marker().apply {
            position = LatLng(
                pb.coordinates["_latitude"] as Double,
                pb.coordinates["_longitude"] as Double
            )
            tag = pb.brandName
            isHideCollidedSymbols = true
            isIconPerspectiveEnabled = true
            width = 155
            height = 170
            icon = when (pb.brandName) {
                "포토매틱" -> {
                    OverlayImage.fromResource(R.drawable.marker_photomatic_off)
                }
                "하루필름" -> {
                    OverlayImage.fromResource(R.drawable.marker_harufilm_off)
                }
                "셀픽스" -> {
                    OverlayImage.fromResource(R.drawable.marker_selfix_off)
                }
                "포토드링크" -> {
                    OverlayImage.fromResource(R.drawable.marker_photodrink_off)
                }
                "포토그레이" -> {
                    OverlayImage.fromResource(R.drawable.marker_photogray_off)
                }
                "포토이즘" -> {
                    OverlayImage.fromResource(R.drawable.marker_photoism_off)
                }
                "인생네컷" -> {
                    OverlayImage.fromResource(R.drawable.marker_lifefourcut_off)
                }
                "비룸" -> {
                    OverlayImage.fromResource(R.drawable.marker_broom_off)
                }
                else -> {
                    MarkerIcons.BLACK.also {
                        com.naver.maps.map.R.drawable.navermap_default_marker_icon_black
                    }
                }
            }
        }
}