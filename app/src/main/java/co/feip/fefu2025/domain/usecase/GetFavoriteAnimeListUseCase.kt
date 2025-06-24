package co.feip.fefu2025.domain.usecase

import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteAnimeListUseCase(private val animeRepository: AnimeRepository) {
    operator fun invoke(): Flow<List<Anime>> {
        return animeRepository.getFavoriteAnimeList()
    }
}