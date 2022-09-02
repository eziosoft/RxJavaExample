package com.netguru.data.remote.models

data class OpenApiResponse(
    val nhits: Int,
    val records: List<Records>,
)
