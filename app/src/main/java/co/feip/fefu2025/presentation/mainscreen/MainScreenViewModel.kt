package co.feip.fefu2025.presentation.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.domain.usecase.GetAnimeListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val getAnimeListUseCase: GetAnimeListUseCase
) : ViewModel() {

    private val _animeListState = MutableStateFlow<List<Anime>>(emptyList())
    val animeListState: StateFlow<List<Anime>> = _animeListState.asStateFlow()

    init {
        loadAnimeList()
    }

    fun loadAnimeList() {
        viewModelScope.launch {
            val animeList = getAnimeListUseCase()
            _animeListState.value = animeList

        }
    }
}