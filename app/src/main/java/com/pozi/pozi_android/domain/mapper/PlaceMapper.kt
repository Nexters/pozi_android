package com.pozi.pozi_android.domain.mapper

import com.pozi.pozi_android.data.remote.spec.Place
import com.pozi.pozi_android.ui.searchLocation.model.SearchModel

object PlaceMapper {
    fun placeToSearchModel(place: Place): SearchModel =
        SearchModel(
            title = place.place_name,
            subTitle = place.address_name,
            longitude = place.x.toDouble(),
            latitude = place.y.toDouble()
        )
}