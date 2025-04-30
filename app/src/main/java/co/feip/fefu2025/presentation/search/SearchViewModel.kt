package co.feip.fefu2025.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.domain.usecase.GetAnimeListUseCase
import co.feip.fefu2025.presentation.common.UiState

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val getAnimeListUseCase: GetAnimeListUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _searchResults = MutableStateFlow<UiState<List<Anime>>>(UiState.Success(emptyList()))
    val searchResults: StateFlow<UiState<List<Anime>>> = _searchResults.asStateFlow()

    private var fullAnimeList: List<Anime>? = null

    init {
        viewModelScope.launch {
            _query
                .debounce(500L)
                .distinctUntilChanged()
                .collectLatest { searchQuery ->
                    performSearch(searchQuery)
                }
        }
    }

    fun updateQuery(newQuery: String) {
        _query.value = newQuery
    }

    private suspend fun performSearch(searchQuery: String) {
        if (searchQuery.isBlank()) {
            _searchResults.value = UiState.Success(emptyList())
            return
        }

        _searchResults.value = UiState.Loading

        if (fullAnimeList == null) {
            fullAnimeList = getAnimeListUseCase()
        }


        val filteredList = fullAnimeList?.filter { anime ->
            anime.title.contains(searchQuery, ignoreCase = true)
        } ?: emptyList()

        _searchResults.value = UiState.Success(filteredList)

    }

    fun retrySearch() {
        val currentQuery = _query.value
        if(currentQuery.isNotBlank()){
            viewModelScope.launch {
                performSearch(currentQuery)
            }
        }
    }
}