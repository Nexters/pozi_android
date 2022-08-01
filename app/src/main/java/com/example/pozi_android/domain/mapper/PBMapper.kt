package com.example.pozi_android.domain.mapper

import android.util.Log
import com.example.pozi_android.data.remote.spec.PBRes
import com.example.pozi_android.domain.entity.PB

object PBMapper {

    fun mapperToPB(PBResult: List<PBRes>): List<PB> {
        var id: Int = 0
        val pbres: MutableList<PB> = mutableListOf()
        PBResult.forEach { it ->
            pbres.add(
                PB(
                    id = id,
                    address = it.address,
                    _latitude = it.coordinates.get("_latitude")!!,
                    _longitude = it.coordinates.get("_longitude")!!,
                    subject = it.subject,
                    brandName = it.brandName
                )
            )
            id += 1
        }
        Log.d("asd",pbres.toList().toString())
        return pbres.toList()
    }
}