package co.feip.fefu2025.data.repository

import co.feip.fefu2025.data.local.FavoriteAnimeDao
import co.feip.fefu2025.data.mapper.toDomain
import co.feip.fefu2025.data.mapper.toFavoriteEntity
import co.feip.fefu2025.data.remote.ApiService
import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.domain.repository.AnimeRepository
import co.feip.fefu2025.domain.model.PaginatedAnimeResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException

class AnimeRepositoryImpl(private val favoriteAnimeDao: FavoriteAnimeDao) : AnimeRepository {

    private val apiService: ApiService by lazy { ApiService.create() }

    override suspend fun getAnimeList(page: Int): PaginatedAnimeResult {
        try {
            val response = apiService.getTopAnime(page = page)
            return PaginatedAnimeResult(
                animeList = response.data.map { it.toDomain() },
                hasNextPage = response.pagination.has_next_page,
                currentPageIfKnown = response.pagination.current_page
            )
        } catch (e: Exception) {
            throw IOException("Не удалось загрузить список аниме: ${e.localizedMessage}", e)
        }
    }

    override suspend fun getAnimeDetailsFromApi(id: Int): Anime? {
        try {
            val response = apiService.getAnimeDetails(id)
            return response.data.toDomain()
        } catch (e: Exception) {
            throw IOException("Не удалось загрузить детали для аниме ID: $id. Ошибка: ${e.localizedMessage}", e)
        }
    }

    override suspend fun getAnimeRecommendations(animeId: Int): List<Anime> {
        try {
            val response = apiService.getAnimeRecommendations(animeId)
            return response.data.take(15).mapNotNull { recommendationEntry ->
                recommendationEntry.entry.toDomain()
            }
        } catch (e: Exception) {
            throw IOException("Не удалось загрузить рекомендации для аниме ID: $animeId. Ошибка: ${e.localizedMessage}", e)
        }
    }

    override suspend fun searchAnime(query: String, page: Int): PaginatedAnimeResult {
        if (query.isBlank()) {
            return PaginatedAnimeResult(emptyList(), false)
        }
        try {
            val response = apiService.searchAnime(query = query, page = page)
            return PaginatedAnimeResult(
                animeList = response.data.map { it.toDomain() },
                hasNextPage = response.pagination.has_next_page,
                currentPageIfKnown = response.pagination.current_page
            )
        } catch (e: Exception) {
            throw IOException("Не удалось выполнить поиск по запросу \"$query\": ${e.localizedMessage}", e)
        }
    }


    override fun getFavoriteAnimeList(): Flow<List<Anime>> {
        return favoriteAnimeDao.getFavoriteAnimeList().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun addAnimeToFavorites(anime: Anime) {
        favoriteAnimeDao.addFavorite(anime.toFavoriteEntity())
    }

    override suspend fun removeAnimeFromFavorites(animeId: Int) {
        favoriteAnimeDao.removeFavorite(animeId)
    }

    override fun isAnimeFavorite(animeId: Int): Flow<Boolean> {
        return favoriteAnimeDao.getFavoriteById(animeId).map { it != null }
    }

    override suspend fun getFavoriteAnimeById(id: Int): Anime? {
        return favoriteAnimeDao.getFavoriteByIdOnce(id)?.toDomain()
    }
}