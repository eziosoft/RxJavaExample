package com.netguru.data.remote.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Records(
    val datasetid: String,
    val recordid: String,
    val fields: Fields,
    val geometry: Geometry,
    val record_timestamp: String
) : Parcelable
