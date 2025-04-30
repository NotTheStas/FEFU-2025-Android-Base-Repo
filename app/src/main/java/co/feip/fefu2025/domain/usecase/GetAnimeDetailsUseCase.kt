package co.feip.fefu2025.domain.usecase

import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.domain.repository.AnimeRepository

class GetAnimeDetailsUseCase(private val animeRepository: AnimeRepository) {

    suspend operator fun invoke(id: Int): Anime? {
        return animeRepository.getAnimeById(id)
    }
}