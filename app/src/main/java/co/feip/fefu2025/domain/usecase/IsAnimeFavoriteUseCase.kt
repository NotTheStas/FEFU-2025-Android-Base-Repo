package co.feip.fefu2025.domain.usecase

import co.feip.fefu2025.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow

class IsAnimeFavoriteUseCase(private val animeRepository: AnimeRepository) {
    operator fun invoke(animeId: Int): Flow<Boolean> {
        return animeRepository.isAnimeFavorite(animeId)
    }
}