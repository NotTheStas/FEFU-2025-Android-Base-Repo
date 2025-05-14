package co.feip.fefu2025.presentation.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.domain.usecase.GetAnimeListUseCase
import co.feip.fefu2025.presentation.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val getAnimeListUseCase: GetAnimeListUseCase) : ViewModel() {

    private val _animeListState = MutableStateFlow<UiState<List<Anime>>>(UiState.Loading)
    val animeListState: StateFlow<UiState<List<Anime>>> = _animeListState.asStateFlow()

    private val _isLoadingNextPage = MutableStateFlow(false)
    val isLoadingNextPage: StateFlow<Boolean> = _isLoadingNextPage.asStateFlow()

    private val _canLoadMore = MutableStateFlow(true)
    val canLoadMore: StateFlow<Boolean> = _canLoadMore.asStateFlow()

    private var currentPage = 1

    init {
        fetchAnimeListInternal(isInitialLoad = true)
    }

    private fun fetchAnimeListInternal(isInitialLoad: Boolean) {
        if (_isLoadingNextPage.value || (!_canLoadMore.value && !isInitialLoad)) return

        _isLoadingNextPage.value = true
        if (isInitialLoad) {
            currentPage = 1
            _canLoadMore.value = true
            _animeListState.value = UiState.Loading
        }

        viewModelScope.launch {
            try {
                val paginatedResult = getAnimeListUseCase(currentPage)
                var newAnimeListFromApi = paginatedResult.animeList

                val distinctNewAnimeListFromApi = newAnimeListFromApi
                    .distinctBy { it.id }
                if (newAnimeListFromApi.size != distinctNewAnimeListFromApi.size) {
                    newAnimeListFromApi = distinctNewAnimeListFromApi
                }

                _canLoadMore.value = paginatedResult.hasNextPage

                val currentList = if (isInitialLoad || _animeListState.value !is UiState.Success) {
                    emptyList()
                } else {
                    (_animeListState.value as UiState.Success<List<Anime>>).data
                }

                if (newAnimeListFromApi.isNotEmpty() || isInitialLoad) {
                    _animeListState.value = UiState.Success(currentList + newAnimeListFromApi)
                } else if (currentList.isEmpty() && !paginatedResult.hasNextPage) {
                    _animeListState.value = UiState.Success(emptyList())
                }


                if (paginatedResult.hasNextPage) {
                    currentPage++
                }
            } catch (e: Exception) {
                if (isInitialLoad) {
                    _animeListState.value = UiState.Error(e.message ?: "Произошла неизвестная ошибка")
                }
            } finally {
                _isLoadingNextPage.value = false
            }
        }
    }

    fun loadNextPage() {
        fetchAnimeListInternal(isInitialLoad = false)
    }

    fun fetchAnimeList() {
        fetchAnimeListInternal(isInitialLoad = true)
    }
}