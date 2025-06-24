package co.feip.fefu2025.presentation.animedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.domain.usecase.AddFavoriteUseCase
import co.feip.fefu2025.domain.usecase.GetAnimeDetailsUseCase
import co.feip.fefu2025.domain.usecase.GetAnimeRecommendationsUseCase
import co.feip.fefu2025.domain.usecase.GetFavoriteAnimeByIdUseCase
import co.feip.fefu2025.domain.usecase.IsAnimeFavoriteUseCase
import co.feip.fefu2025.domain.usecase.RemoveFavoriteUseCase
import co.feip.fefu2025.presentation.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AnimeDetailsViewModel(
    private val getAnimeDetailsUseCase: GetAnimeDetailsUseCase,
    private val getAnimeRecommendationsUseCase: GetAnimeRecommendationsUseCase,
    private val getFavoriteAnimeByIdUseCase: GetFavoriteAnimeByIdUseCase,
    private val isAnimeFavoriteUseCase: IsAnimeFavoriteUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val animeId: Int = checkNotNull(savedStateHandle["animeId"])

    private val _animeDetailsState = MutableStateFlow<UiState<Anime>>(UiState.Loading)
    val animeDetailsState: StateFlow<UiState<Anime>> = _animeDetailsState.asStateFlow()

    private val _favoriteDetailsState = MutableStateFlow<UiState<Anime>>(UiState.Loading)
    val favoriteDetailsState: StateFlow<UiState<Anime>> = _favoriteDetailsState.asStateFlow()

    private val _recommendationsState = MutableStateFlow<List<Anime>>(emptyList())
    val recommendationsState: StateFlow<List<Anime>> = _recommendationsState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    init {
        loadAnimeDetailsAndRecommendations()
        observeFavoriteStatus()
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

    fun loadFavoriteAnimeDetails(id: Int) {
        _favoriteDetailsState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val anime = getFavoriteAnimeByIdUseCase(id)
                if (anime != null) {
                    _favoriteDetailsState.value = UiState.Success(anime)
                } else {
                    _favoriteDetailsState.value = UiState.Error("Избранное аниме с ID $id не найдено")
                }
            } catch (e: Exception) {
                _favoriteDetailsState.value = UiState.Error(e.message ?: "Ошибка загрузки из БД")
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

    private fun observeFavoriteStatus() {
        isAnimeFavoriteUseCase(animeId)
            .onEach { isFav -> _isFavorite.value = isFav }
            .launchIn(viewModelScope)
    }

    fun toggleFavoriteStatus() {
        viewModelScope.launch {
            val currentAnime = (_animeDetailsState.value as? UiState.Success)?.data
            if (currentAnime != null) {
                if (_isFavorite.value) {
                    removeFavoriteUseCase(currentAnime.id)
                } else {
                    addFavoriteUseCase(currentAnime)
                }
            }
        }
    }
}