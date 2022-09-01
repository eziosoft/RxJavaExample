package com.netguru.remote

import com.netguru.remote.openApi.OpenApiResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenApi {
    companion object {
        const val BASE_URL = "https://opendata.paris.fr/"
        const val DEFAULT_LOCATION = "48.844248136078164,2.389387952186405,20000"
        const val DATASET_FILMING_LOCATIONS_PARIS = "lieux-de-tournage-a-paris"
        val FIELDS = listOf(
            "annee_tournage",
            "type_tournage",
            "nom_tournage",
            "nom_realisateur",
            "nom_producteur",
            "ardt_lieu",
            "date_debut",
            "date_fin"
        )
    }

    @GET("api/records/1.0/search/")
    fun getData(
        @Query("dataset") dataset: String,
        @Query("facet") fields: List<String>,
        @Query("geofilter.distance") geofilterDistance: String,
        @Query("rows") rows: Int = 10000
    ): Observable<OpenApiResponse>
}
