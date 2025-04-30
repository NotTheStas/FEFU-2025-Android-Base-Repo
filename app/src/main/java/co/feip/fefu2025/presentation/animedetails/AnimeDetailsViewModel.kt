package co.feip.fefu2025.presentation.animedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.domain.usecase.GetAnimeDetailsUseCase
import co.feip.fefu2025.domain.usecase.GetAnimeListUseCase
import co.feip.fefu2025.presentation.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnimeDetailsViewModel(
    private val getAnimeDetailsUseCase: GetAnimeDetailsUseCase,
    private val getAnimeListUseCase: GetAnimeListUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val animeId: Int? = savedStateHandle["animeId"]

    private val _animeDetailsState = MutableStateFlow<UiState<Anime>>(UiState.Loading)
    val animeDetailsState: StateFlow<UiState<Anime>> = _animeDetailsState.asStateFlow()

    private val _recommendationsState = MutableStateFlow<List<Anime>>(emptyList())
    val recommendationsState: StateFlow<List<Anime>> = _recommendationsState.asStateFlow()


    init {
        loadAnimeDetails()
        loadRecommendations()
    }

    fun loadAnimeDetails() {
        if (animeId == null) {
            _animeDetailsState.value = UiState.Error("Не удалось получить ID аниме")
            return
        }
        _animeDetailsState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val anime = getAnimeDetailsUseCase(animeId)
                if (anime != null) {
                    _animeDetailsState.value = UiState.Success(anime)
                } else {
                    _animeDetailsState.value = UiState.Error("Аниме с ID $animeId не найдено")
                }
            } catch (e: Exception) {
                _animeDetailsState.value = UiState.Error(e.message ?: "Ошибка загрузки деталей")
            }
        }
    }

    private fun loadRecommendations() {
        if (animeId == null) return
        viewModelScope.launch {
            try {
                val allAnime = getAnimeListUseCase()
                _recommendationsState.value = allAnime.filter { it.id != animeId }
            } catch (e: Exception) {
                _recommendationsState.value = emptyList()
                println("Error loading recommendations: ${e.message}")
            }
        }
    }
}