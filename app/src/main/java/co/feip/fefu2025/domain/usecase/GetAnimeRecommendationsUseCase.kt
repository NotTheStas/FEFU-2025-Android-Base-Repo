package co.feip.fefu2025.domain.usecase

import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.domain.repository.AnimeRepository

class GetAnimeRecommendationsUseCase(private val animeRepository: AnimeRepository) {
    suspend operator fun invoke(animeId: Int): List<Anime> {
        return animeRepository.getAnimeRecommendations(animeId)
    }
}