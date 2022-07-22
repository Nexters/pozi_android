package com.example.pozi_android.domain.mapper

import com.example.pozi_android.data.remote.spec.TestRes
import com.example.pozi_android.domain.entity.PB


object TestMapper {
    fun mapperTotest(PBres: TestRes): List<PB> {
        val PBlist = mutableListOf<PB>()
        PBres.locations.forEach { it ->
            PBlist.add(
                PB(
                    id = it.id,
                    address = it.address,
                    _latitude = it.lat,
                    _longitude = it.lng,
                    subject = it.name
                )
            )
        }
        return PBlist.toList()
    }
}