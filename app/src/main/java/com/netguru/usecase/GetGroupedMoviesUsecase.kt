package com.netguru.usecase

import com.netguru.domain.Place
import com.netguru.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class GetGroupedMoviesUsecase(private val repository: Repository) {
    fun invoke(onResponse: (Result<Map<String, List<Place>>>) -> Unit) =
        repository.getMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { result ->
                result.records.map {
                    Place(
                        filmTitle = it.fields.nom_tournage ?: "??",
                        date = it.fields.date_debut ?: "",
                        0.0, 0.0
                    )
                }.groupBy { it.filmTitle }
            }
            .subscribe(
                { response -> onResponse(Result.success(response)) },
                { t -> Result.failure<Exception>(t) })
}