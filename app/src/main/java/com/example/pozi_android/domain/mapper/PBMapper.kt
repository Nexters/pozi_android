package com.example.pozi_android.domain.mapper

import android.util.Log
import com.example.pozi_android.data.remote.spec.PBRes
import com.example.pozi_android.domain.entity.PB

object PBMapper {

    fun mapperToPB(PBResult: MutableList<PB>, pbres: List<PBRes>, id: Int, brand: String) {
        var id: Int = id
        pbres.forEach { it ->
            PBResult.add(
                PB(
                    id = id,
                    address = it.address,
                    _latitude = it.coordinates.get("_latitude")!!,
                    _longitude = it.coordinates.get("_longitude")!!,
                    subject = it.subject,
                    brand = brand
                )
            )
            id += 1
        }
    }
}