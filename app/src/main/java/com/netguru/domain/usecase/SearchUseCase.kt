package com.netguru.domain.usecase

import com.netguru.domain.Place
import com.netguru.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val repository: Repository) {
    fun invoke(searchText: String, onResponse: (Result<Map<String, List<Place>>>) -> Unit) =
        repository.getMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { result ->
                result.records.filter {
                    it.fields.nom_tournage?.uppercase()?.contains(searchText.uppercase())
                        ?: false
                }.map {
                    Place(
                        filmTitle = it.fields.nom_tournage ?: "??",
                        startDate = it.fields.date_debut ?: "",
                        endDate = it.fields.date_fin ?: "",
                        0.0, 0.0
                    )
                }.groupBy { it.filmTitle }
            }
            .subscribe(
                { response -> onResponse(Result.success(response)) },
                { t -> Result.failure<Exception>(t) }
            )
}
