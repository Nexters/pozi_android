package com.example.pozi_android.domain.mapper

import com.example.pozi_android.data.remote.spec.PBRes
import com.example.pozi_android.domain.entity.PB


object PBMapper {
    fun mapperToPB(PBres: PBRes): List<PB> {
        val PBlist = mutableListOf<PB>()
        PBres.locations.forEach { it ->
            PBlist.add(
                PB(
                    id = it.id,
                    address = it.address,
                    lat = it.lat,
                    lng = it.lng,
                    name = it.name
                )
            )
        }
        return PBlist.toList()
    }
}