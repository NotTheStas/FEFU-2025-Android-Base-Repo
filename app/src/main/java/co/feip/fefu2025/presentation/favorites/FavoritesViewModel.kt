package co.feip.fefu2025.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.domain.usecase.GetFavoriteAnimeListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class FavoritesUiState(
    val isLoading: Boolean = true,
    val animeList: List<Anime> = emptyList(),
    val error: String? = null
)

class FavoritesViewModel(
    private val getFavoriteAnimeListUseCase: GetFavoriteAnimeListUseCase
) : ViewModel() {

    private val _favoritesState = MutableStateFlow(FavoritesUiState())
    val favoritesState: StateFlow<FavoritesUiState> = _favoritesState.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        getFavoriteAnimeListUseCase()
            .onEach { animeList ->
                _favoritesState.value = FavoritesUiState(isLoading = false, animeList = animeList)
            }
            .catch { e ->
                _favoritesState.value = FavoritesUiState(isLoading = false, error = e.message)
            }
            .launchIn(viewModelScope)
    }
}