package com.example.pozi_android.domain.mapper

import com.example.pozi_android.data.remote.spec.PBResponce
import com.example.pozi_android.domain.entity.PB

object PBMapper {
    fun mapperToPB(PBResponce: PBResponce): PB {
        return PB(
            id = PBResponce.id,
            address = PBResponce.address,
            lat = PBResponce.lat,
            lng = PBResponce.lng,
            name = PBResponce.name
        )
    }
}