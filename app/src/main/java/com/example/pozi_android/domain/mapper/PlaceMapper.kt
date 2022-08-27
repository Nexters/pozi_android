package com.example.pozi_android.domain.mapper

import com.example.pozi_android.data.remote.spec.Place
import com.example.pozi_android.domain.entity.PBEntity
import com.example.pozi_android.ui.searchLocation.SearchModel
import com.google.firebase.firestore.DocumentSnapshot
import kotlin.math.roundToLong

object PlaceMapper {
    fun placeToSearchModel(place: Place): SearchModel =
        SearchModel(
            title = place.place_name,
            subTitle = place.address_name,
            longitude = place.x.toDouble(),
            latitude = place.y.toDouble()
        )
}