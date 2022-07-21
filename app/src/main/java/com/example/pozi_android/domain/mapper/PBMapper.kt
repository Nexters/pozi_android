package com.example.pozi_android.domain.mapper

import com.example.pozi_android.R
import com.example.pozi_android.data.remote.network.PBListRes
import com.example.pozi_android.data.remote.spec.PBResponce
import com.example.pozi_android.domain.entity.PB
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.MarkerIcons

object PBMapper {
    fun mapperToPB(PBres: PBListRes): List<PB> {
        val PBList = mutableListOf<PB>()
        PBres.locations.forEach { it ->
            PBList.add(
                PB(
                    id = it.id,
                    address = it.address,
                    lat = it.lat,
                    lng = it.lng,
                    name = it.name
                )
            )
        }
        return PBList.toList()
    }
}