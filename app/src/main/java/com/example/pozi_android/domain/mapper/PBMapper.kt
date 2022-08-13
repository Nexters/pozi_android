package com.example.pozi_android.domain.mapper

import android.util.Log
import com.example.pozi_android.data.remote.spec.PBRes
import com.example.pozi_android.domain.entity.PBEntity
import com.google.firebase.firestore.DocumentSnapshot

object PBMapper {
    fun mapToEntity(id: Int, snap: DocumentSnapshot): PBEntity =
        PBEntity(
            id = id,
            address = snap.get("address") as String,
            _latitude = (snap.get("coordinates") as Map<String, Double>).get("_latitude") as Double,
            _longitude = (snap.get("coordinates") as Map<String, Double>).get("_longitude") as Double,
            subject = snap.get("subject") as String,
            brandName = snap.get("brandName") as String,
            phoneNumber = snap.get("phoneNumber") as String
        )

    fun mapperToPB(id: Int, pb: PBRes.PB): PBEntity = PBEntity(
        id = id,
        address = pb.address,
        _latitude = pb.coordinates["_latitude"] as Double,
        _longitude = pb.coordinates["_longitude"] as Double,
        subject = pb.subject,
        brandName = pb.brandName,
        phoneNumber = id.toString()
    )
}