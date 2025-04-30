package co.feip.fefu2025.presentation.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.domain.usecase.GetAnimeListUseCase
import co.feip.fefu2025.presentation.common.UiState

class MainViewModel(private val getAnimeListUseCase: GetAnimeListUseCase) : ViewModel() {

    private val _animeListState = MutableStateFlow<UiState<List<Anime>>>(UiState.Loading)
    val animeListState: StateFlow<UiState<List<Anime>>> = _animeListState.asStateFlow()

    init {
        fetchAnimeList()
    }

    fun fetchAnimeList() {
        _animeListState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val animeList = getAnimeListUseCase()
                _animeListState.value = UiState.Success(animeList)
            } catch (e: Exception) {
                _animeListState.value = UiState.Error(e.message ?: "Произошла неизвестная ошибка")
            }
        }
    }
}