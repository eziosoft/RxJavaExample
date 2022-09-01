package com.netguru.remote.openApi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Fields(
    val annee_tournage: Int?,
    val coord_y: Double?,
    /***
     * used as distance from current location
     */
    var coord_x: Double?,
    val date_fin: String?,
    val type_tournage: String?,
    val nom_producteur: String?,
    val geo_point_2d: List<Double>,
    val nom_tournage: String?,
    val nom_realisateur: String?,
    val id_lieu: String?,
    val adresse_lieu: String?,
    val geo_shape: Geo_shape,
    val dist: Double?,
    val date_debut: String?,
    val ardt_lieu: Int?,


    ) : Parcelable