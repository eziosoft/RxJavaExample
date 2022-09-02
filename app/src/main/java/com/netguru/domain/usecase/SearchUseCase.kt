package com.netguru.domain.usecase

import com.netguru.data.MovieRepository
import com.netguru.domain.entities.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val movieRepository: MovieRepository) {
    fun invoke(searchText: String, onResponse: (Result<Map<String, List<Movie>>>) -> Unit) =
        movieRepository.getMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { result ->
                result.records.filter {
                    it.fields.nom_tournage?.uppercase()?.contains(searchText.uppercase())
                        ?: false
                }.map {
                    Movie(
                        movieTitle = it.fields.nom_tournage ?: "??",
                        startDate = it.fields.date_debut ?: "",
                        endDate = it.fields.date_fin ?: ""
                    )
                }.groupBy { it.movieTitle }
            }
            .subscribe(
                { response -> onResponse(Result.success(response)) },
                { t -> Result.failure<Exception>(t) }
            )
}
