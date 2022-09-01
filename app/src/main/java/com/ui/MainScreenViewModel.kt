package com.ui

import androidx.lifecycle.ViewModel
import com.netguru.domain.Place
import com.netguru.repository.Repository
import com.netguru.usecase.GetGroupedMoviesUsecase
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.*

data class ViewState(
    val places: Map<String, List<Place>> = emptyMap(),
    val errorMessage: String? = null,
    val loading: Boolean = false
)

class MainViewModel(
    private val repository: Repository = Repository(),
    getGroupedMoviesUsecase: GetGroupedMoviesUsecase = GetGroupedMoviesUsecase(repository)
) : ViewModel() {

    private val _viewStateFlow = MutableStateFlow(ViewState())
    val viewStateFlow = _viewStateFlow.asStateFlow()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        _viewStateFlow.tryEmit(ViewState(loading = true))

        compositeDisposable.add(
            getGroupedMoviesUsecase.invoke { result ->
                if (result.isSuccess) {
                    _viewStateFlow.tryEmit(ViewState(places = result.getOrDefault(emptyMap())))
                }

                if (result.isFailure) {
                    _viewStateFlow.tryEmit(ViewState(errorMessage = result.exceptionOrNull()?.message))
                }
            }
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
