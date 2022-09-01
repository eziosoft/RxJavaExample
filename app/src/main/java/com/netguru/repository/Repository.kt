package com.netguru.repository

import com.netguru.remote.OpenApi

class Repository (private val openApiClient: OpenApi) {

    fun getMovies() = openApiClient.getData(
        dataset = OpenApi.DATASET_FILMING_LOCATIONS_PARIS,
        fields = OpenApi.FIELDS,
        geofilterDistance = OpenApi.DEFAULT_LOCATION
    )
}
