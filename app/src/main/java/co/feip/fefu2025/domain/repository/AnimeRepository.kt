package co.feip.fefu2025.domain.repository

import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.domain.model.PaginatedAnimeResult
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    suspend fun getAnimeList(page: Int): PaginatedAnimeResult
    suspend fun getAnimeDetailsFromApi(id: Int): Anime?
    suspend fun getAnimeRecommendations(animeId: Int): List<Anime>
    suspend fun searchAnime(query: String, page: Int): PaginatedAnimeResult

    fun getFavoriteAnimeList(): Flow<List<Anime>>
    suspend fun addAnimeToFavorites(anime: Anime)
    suspend fun removeAnimeFromFavorites(animeId: Int)
    fun isAnimeFavorite(animeId: Int): Flow<Boolean>
    suspend fun getFavoriteAnimeById(id: Int): Anime?
}