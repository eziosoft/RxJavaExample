package com.netguru.repository

import com.netguru.remote.OpenApi
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class Repository {
    private val retrofitClient by lazy {
        Retrofit.Builder()
            .baseUrl(OpenApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private val openApiClient by lazy { retrofitClient.create(OpenApi::class.java) }

    fun getMovies() = openApiClient.getData(
        dataset = OpenApi.DATASET_FILMING_LOCATIONS_PARIS,
        fields = OpenApi.FIELDS,
        geofilterDistance = OpenApi.DEFAULT_LOCATION
    )
}
