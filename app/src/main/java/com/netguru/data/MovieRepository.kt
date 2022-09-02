package com.netguru.data

import com.netguru.data.remote.OpenApi

class MovieRepository(private val openApiClient: OpenApi) {

    fun getMovies() = openApiClient.getData(
        dataset = OpenApi.DATASET_FILMING_LOCATIONS_PARIS,
        fields = OpenApi.FIELDS,
        geofilterDistance = OpenApi.DEFAULT_LOCATION
    )
}
