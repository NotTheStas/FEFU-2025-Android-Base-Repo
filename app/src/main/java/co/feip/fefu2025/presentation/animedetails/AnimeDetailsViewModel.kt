package co.feip.fefu2025.presentation.animedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.domain.usecase.GetAnimeDetailsUseCase
import co.feip.fefu2025.domain.usecase.GetAnimeRecommendationsUseCase
import co.feip.fefu2025.presentation.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnimeDetailsViewModel(
    private val getAnimeDetailsUseCase: GetAnimeDetailsUseCase,
    private val getAnimeRecommendationsUseCase: GetAnimeRecommendationsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val animeId: Int = checkNotNull(savedStateHandle["animeId"])

    private val _animeDetailsState = MutableStateFlow<UiState<Anime>>(UiState.Loading)
    val animeDetailsState: StateFlow<UiState<Anime>> = _animeDetailsState.asStateFlow()

    private val _recommendationsState = MutableStateFlow<List<Anime>>(emptyList())
    val recommendationsState: StateFlow<List<Anime>> = _recommendationsState.asStateFlow()

    init {
        loadAnimeDetailsAndRecommendations()
    }

    private fun loadAnimeDetailsAndRecommendations() {
        loadAnimeDetails()
        loadRecommendations()
    }


    fun loadAnimeDetails() {
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
        viewModelScope.launch {
            try {
                val recommendations = getAnimeRecommendationsUseCase(animeId)
                _recommendationsState.value = recommendations
            } catch (e: Exception) {
                _recommendationsState.value = emptyList()
            }
        }
    }
}