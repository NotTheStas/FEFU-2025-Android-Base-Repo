package co.feip.fefu2025.domain.usecase

import co.feip.fefu2025.domain.repository.AnimeRepository

class RemoveFavoriteUseCase(private val animeRepository: AnimeRepository) {
    suspend operator fun invoke(animeId: Int) {
        animeRepository.removeAnimeFromFavorites(animeId)
    }
}