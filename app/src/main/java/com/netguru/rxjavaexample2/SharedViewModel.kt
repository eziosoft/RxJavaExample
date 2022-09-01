package com.netguru.rxjavaexample2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netguru.domain.Place
import com.netguru.domain.usecase.GetGroupedMoviesUsecase
import com.netguru.domain.usecase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ViewState(
    val places: Map<String, List<Place>> = emptyMap(),
    val errorMessage: String? = null,
    val loading: Boolean = false
)

@HiltViewModel
class SharedViewModel @Inject constructor(
    getGroupedMoviesUseCase: GetGroupedMoviesUsecase,
    private val searchUseCase: SearchUseCase
) : ViewModel() {
    private val _viewStateFlow = MutableStateFlow(ViewState())
    val viewStateFlow = _viewStateFlow.asStateFlow()
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
        viewModelScope.launch {
            _viewStateFlow.emit(ViewState(loading = true))
        }
    }

    private fun setViewState(result: Result<Map<String, List<Place>>>) {
        viewModelScope.launch {
            when {
                result.isSuccess ->
                    _viewStateFlow.emit(
                        ViewState(places = result.getOrDefault(emptyMap()))
                    )
                result.isFailure ->
                    _viewStateFlow.emit(
                        ViewState(errorMessage = result.exceptionOrNull()?.message)
                    )
            }
        }
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
