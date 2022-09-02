package com.netguru.data.remote.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Geo_shape(
    val type: String,
    val coordinates: List<Double>
) : Parcelable
