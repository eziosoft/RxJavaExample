package com.netguru.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netguru.domain.entities.Movie
import com.netguru.domain.usecase.GetGroupedMoviesUseCase
import com.netguru.domain.usecase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ViewState(
    val places: Map<String, List<Movie>> = emptyMap(),
    val errorMessage: String? = null,
    val loading: Boolean = false
)

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    getGroupedMoviesUseCase: GetGroupedMoviesUseCase,
    private val searchUseCase: SearchUseCase
) : ViewModel() {
    val viewStateSubject: BehaviorSubject<ViewState> = BehaviorSubject.create()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        showLoading()
        compositeDisposable.add(
            getGroupedMoviesUseCase.invoke { result ->
                setViewState(result)
            }
        )
    }

    fun search(text: String) {
        showLoading()
        searchUseCase.invoke(text) { result ->
            setViewState(result)
        }
    }

    private fun showLoading() {
        viewStateSubject.onNext(ViewState(loading = true))
    }

    private fun setViewState(result: Result<Map<String, List<Movie>>>) {
        viewModelScope.launch {
            when {
                result.isSuccess ->
                    viewStateSubject.onNext(ViewState(places = result.getOrDefault(emptyMap())))
                result.isFailure ->
                    viewStateSubject.onNext(ViewState(errorMessage = result.exceptionOrNull()?.message))
            }
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
