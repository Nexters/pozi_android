package com.example.pozi_android.domain.mapper

import com.example.pozi_android.domain.entity.PB
import com.google.firebase.firestore.DocumentSnapshot

object PBMapper {
    fun mapToEntity(id: Int, snap: DocumentSnapshot): PB =
        PB(
            id = id,
            address = snap.get("address") as String,
            _latitude = (snap.get("coordinates") as Map<String, Double>).get("_latitude") as Double,
            _longitude = (snap.get("coordinates") as Map<String, Double>).get("_longitude") as Double,
            subject = snap.get("subject") as String,
            brandName = snap.get("brandName") as String
        )
}