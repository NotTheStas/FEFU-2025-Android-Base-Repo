package co.feip.fefu2025.presentation.animedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.domain.usecase.GetAnimeDetailsUseCase
import co.feip.fefu2025.domain.usecase.GetAnimeListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnimeDetailsViewModel(
    private val getAnimeDetailsUseCase: GetAnimeDetailsUseCase,
    private val getAnimeListUseCase: GetAnimeListUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val animeId: Int = checkNotNull(savedStateHandle["animeId"])

    private val _animeDetailsState = MutableStateFlow<Anime?>(null)
    val animeDetailsState: StateFlow<Anime?> = _animeDetailsState.asStateFlow()

    private val _recommendationsState = MutableStateFlow<List<Anime>>(emptyList())
    val recommendationsState: StateFlow<List<Anime>> = _recommendationsState.asStateFlow()


    init {
        loadAnimeDetails()
        loadRecommendations()
    }

    private fun loadAnimeDetails() {
        viewModelScope.launch {
            val anime = getAnimeDetailsUseCase(animeId)
            _animeDetailsState.value = anime

        }
    }

    private fun loadRecommendations() {
        viewModelScope.launch {
            val allAnime = getAnimeListUseCase()
            _recommendationsState.value = allAnime
                .filter { it.id != animeId }
        }
    }
}
