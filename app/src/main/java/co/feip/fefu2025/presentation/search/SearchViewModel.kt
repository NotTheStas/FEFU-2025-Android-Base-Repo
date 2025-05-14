package co.feip.fefu2025.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.domain.usecase.SearchAnimeUseCase
import co.feip.fefu2025.presentation.common.UiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val searchAnimeUseCase: SearchAnimeUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _searchResults = MutableStateFlow<UiState<List<Anime>>>(UiState.Success(emptyList()))
    val searchResults: StateFlow<UiState<List<Anime>>> = _searchResults.asStateFlow()

    private val _isLoadingNextPage = MutableStateFlow(false)
    val isLoadingNextPage: StateFlow<Boolean> = _isLoadingNextPage.asStateFlow()

    private val _canLoadMore = MutableStateFlow(true)
    val canLoadMore: StateFlow<Boolean> = _canLoadMore.asStateFlow()

    private var currentPage = 1
    private var currentSearchQuery = ""

    init {
        viewModelScope.launch {
            _query
                .debounce(500L)
                .distinctUntilChanged()
                .collectLatest { searchQuery ->
                    currentSearchQuery = searchQuery
                    if (searchQuery.isBlank()) {
                        _searchResults.value = UiState.Success(emptyList())
                        _canLoadMore.value = false
                        currentPage = 1
                    } else {
                        currentPage = 1
                        _canLoadMore.value = true
                        performSearchInternal(searchQuery, isInitialSearch = true)
                    }
                }
        }
    }

    fun updateQuery(newQuery: String) {
        _query.value = newQuery
    }

    private fun performSearchInternal(searchQuery: String, isInitialSearch: Boolean) {
        if (_isLoadingNextPage.value || (!_canLoadMore.value && !isInitialSearch)) return

        _isLoadingNextPage.value = true
        if (isInitialSearch) {
            _searchResults.value = UiState.Loading
        }

        viewModelScope.launch {
            try {
                val paginatedResult = searchAnimeUseCase(searchQuery, currentPage)
                var newResultsFromApi = paginatedResult.animeList

                _canLoadMore.value = paginatedResult.hasNextPage

                val distinctNewResultsFromApi = newResultsFromApi
                    .distinctBy { it.id }
                if (newResultsFromApi.size != distinctNewResultsFromApi.size) {
                    newResultsFromApi = distinctNewResultsFromApi
                }

                val currentList = if (isInitialSearch || _searchResults.value !is UiState.Success) {
                    emptyList()
                } else {
                    (_searchResults.value as UiState.Success<List<Anime>>).data
                }

                if (newResultsFromApi.isNotEmpty() || isInitialSearch) {
                    _searchResults.value = UiState.Success(currentList + newResultsFromApi)
                } else if (currentList.isEmpty() && !paginatedResult.hasNextPage) {
                    _searchResults.value = UiState.Success(emptyList())
                }


                if (paginatedResult.hasNextPage) {
                    currentPage++
                }
            } catch (e: Exception) {
                if (isInitialSearch) {
                    _searchResults.value = UiState.Error(e.message ?: "Ошибка поиска")
                }
            } finally {
                _isLoadingNextPage.value = false
            }
        }
    }

    fun loadNextSearchPage() {
        if (currentSearchQuery.isNotBlank()) {
            performSearchInternal(currentSearchQuery, isInitialSearch = false)
        }
    }

    fun retrySearch() {
        val currentQueryVal = _query.value
        if (currentQueryVal.isNotBlank()) {
            currentPage = 1
            _canLoadMore.value = true
            performSearchInternal(currentQueryVal, isInitialSearch = true)
        }
    }
}