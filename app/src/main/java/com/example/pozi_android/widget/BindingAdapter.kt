package com.example.pozi_android.widget

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.io.IOException
import java.util.*

@BindingAdapter("settext")
fun setText(view: TextView, text: String) {
    view.text = text
}