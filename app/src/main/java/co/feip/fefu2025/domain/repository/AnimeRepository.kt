package co.feip.fefu2025.domain.repository

import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.domain.model.PaginatedAnimeResult

interface AnimeRepository {
    suspend fun getAnimeList(page: Int): PaginatedAnimeResult
    suspend fun getAnimeDetailsFromApi(id: Int): Anime?
    suspend fun getAnimeRecommendations(animeId: Int): List<Anime>
    suspend fun searchAnime(query: String, page: Int): PaginatedAnimeResult
}