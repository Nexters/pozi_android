package com.example.pozi_android.domain.mapper

import android.util.Log
import com.example.pozi_android.data.remote.spec.PBRes
import com.example.pozi_android.domain.entity.PB

object PBMapper {
    fun mapperToPB(pbres: List<PBRes>): List<PB> {
        val PBlist = mutableListOf<PB>()
        Log.d("아무무",pbres.get(0).address)

        var id : Int = 0
        pbres.forEach { it ->
            PBlist.add(
                PB(
                    id = id,
                    address = it.address,
                    _latitude = it.coordinates.get("_latitude")!!,
                    _longitude = it.coordinates.get("_longitude")!!,
                    subject = it.subject
                )
            )
            id += 1
        }

        return PBlist.toList()
    }
}