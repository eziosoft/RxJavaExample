package com.netguru.domain.usecase

import com.netguru.data.MovieRepository
import com.netguru.domain.entities.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetGroupedMoviesUseCase @Inject constructor(private val movieRepository: MovieRepository) {
    fun invoke(onResponse: (Result<Map<String, List<Movie>>>) -> Unit) =
        movieRepository.getMovies()
            .subscribeOn(Schedulers.io())
            .map { result ->
                result.records.map {
                    Movie(
                        movieTitle = it.fields.nom_tournage ?: "??",
                        startDate = it.fields.date_debut ?: "",
                        endDate = it.fields.date_fin ?: ""
                    )
                }.groupBy { it.movieTitle }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response -> onResponse(Result.success(response)) },
                { t -> Result.failure<Exception>(t) }
            )
}
