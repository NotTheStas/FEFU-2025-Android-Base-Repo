package co.feip.fefu2025.domain.model

data class PaginatedAnimeResult(
    val animeList: List<Anime>,
    val hasNextPage: Boolean,
    val currentPageIfKnown: Int? = null
)