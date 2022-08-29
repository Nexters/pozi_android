package com.pozi.pozi_android.domain.mapper

import com.pozi.pozi_android.R
import com.pozi.pozi_android.domain.entity.PBEntity
import com.pozi.pozi_android.ui.main.model.CustomMarkerModel
import com.google.firebase.firestore.DocumentSnapshot
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.MarkerIcons
import kotlin.math.roundToLong

object PBEntityMapper {
    fun mapperToPBEntity(dis: Double, snap: DocumentSnapshot): PBEntity =
        PBEntity(
            coordinates = snap["coordinates"] as Map<String, Double>,
            address = snap["address"] as String,
            subject = snap.get("subject") as String,
            brandName = snap.get("brandName") as String,
            distance = dis.roundToLong()
        )

    fun PBEntityToCustomMarker(id: Long, pbEntity: PBEntity): CustomMarkerModel =
        CustomMarkerModel(
            id = id,
            marker = PBEntityToMarker(id,pbEntity),
            address = pbEntity.address,
            subject = pbEntity.subject,
            brandName = pbEntity.brandName,
            distance = pbEntity.distance
        )

    fun PBEntityToMarker(id: Long, pbEntity: PBEntity): Marker =
        Marker().apply {
            position = LatLng(
                pbEntity.coordinates["_latitude"] as Double,
                pbEntity.coordinates["_longitude"] as Double
            )
            tag = id
            isHideCollidedSymbols = true
            isIconPerspectiveEnabled = true
            width = 155
            height = 170
            icon = when (pbEntity.brandName) {
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
                "비룸스튜디오" -> {
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